package socialnetwork.beans.impl;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import socialnetwork.beans.AlbumBean;
import socialnetwork.beans.UserBean;
import socialnetwork.beans.ListBean;
import socialnetwork.dto.creation.LanguageRegistrationDto;
import socialnetwork.dto.creation.UserRegistrationDto;
import socialnetwork.entities.*;
import socialnetwork.events.FriendEvent;
import socialnetwork.exceptions.ObjectsNotFoundException;
import socialnetwork.exceptions.ValidationException;
import socialnetwork.helpers.Constants;
import socialnetwork.helpers.EmailHelper;
import socialnetwork.helpers.StringHelper;
import socialnetwork.repositories.FriendRequestRepository;
import socialnetwork.repositories.LanguageUserRepository;
import socialnetwork.repositories.UserRepository;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Roman on 20.08.2017.
 */
@Component
public class UserBeanImpl implements UserBean {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LanguageUserRepository languageUserRepository;
    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private AlbumBean albumBean;
    @Autowired
    private ListBean listBean;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private TokenStore tokenStore;

    public enum ValidationMode {
        CREATE,
        EDIT
    }

    @Override
    public Boolean checkLogin(String login) {
        return userRepository.findUserByLogin(StringHelper.replaceSymbols(login)) == null;
    }

    @Override
    public Boolean checkEmail(String email) {
        return userRepository.findUserByEmailAndConfirmedIsTrue(email) == null;
    }

    @Override
    @Transactional
    public User registerUser(UserRegistrationDto userRegistrationDto, MultipartFile multipartFile, String baseUrl, List<String> errors) {
        if (validateUserDto(userRegistrationDto, ValidationMode.CREATE, errors)) {
            replaceSpecialSymbols(userRegistrationDto);
            Country country = listBean.getCountry(userRegistrationDto.getCountry());
            City city = listBean.getCity(userRegistrationDto.getCity());
            Gender gender = listBean.getGender(userRegistrationDto.getGender());
            if (country == null) {
                errors.add("Country with id: " + userRegistrationDto.getCountry() + " doesn't exist");
            }
            if (city == null) {
                errors.add("City with id: " + userRegistrationDto.getCity() + " doesn't exist");
            }
            if (gender == null) {
                errors.add("Gender with id: " + userRegistrationDto.getGender() + " doesn't exist");
            }
            if (userRepository.findUserByLogin(userRegistrationDto.getLogin()) != null) {
                errors.add("Login " + userRegistrationDto.getLogin() + " already in use");
            }
            Map<Language, LanguageLevel> languageLanguageLevelMap = new HashMap<>();
            if (userRegistrationDto.getLanguages() != null || userRegistrationDto.getLanguageLevels() != null) {
                languageLanguageLevelMap = validateLanguageDtoAndFillLanguageMap(
                        new LanguageRegistrationDto(userRegistrationDto.getLanguages(), userRegistrationDto.getLanguageLevels()), errors);
            }

            if (!errors.isEmpty()) {
                return null;
            }
            User user = new User();
            user.setLogin(userRegistrationDto.getLogin());
            user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
            user.setName(userRegistrationDto.getName());
            user.setEmail(userRegistrationDto.getEmail());
            try {
                user.setBirthday(FORMAT.parse(userRegistrationDto.getBirthday()));
            } catch (ParseException ex) {
                //it never happens
            }
            user.setDescription(userRegistrationDto.getDescription());
            user.setCountry(country);
            user.setCity(city);
            user.setGender(gender);
            user.setConfirmed(false);
            user.setActivationToken(UUID.randomUUID().toString());
            user.setRole(2);
            user.setIsLocked(false);
            user.setOnline(false);
            user = userRepository.save(user);
            if (!languageLanguageLevelMap.isEmpty()) {
                List<LanguageUser> languageUsers = new ArrayList<>();
                for (Map.Entry<Language, LanguageLevel> entry : languageLanguageLevelMap.entrySet()) {
                    languageUsers.add(new LanguageUser(user, entry.getKey(), entry.getValue()));
                }
                languageUserRepository.save(languageUsers);
            }
            Album album = albumBean.createAlbum(user.getId(), "Main");
            if (!multipartFile.isEmpty()) {
                albumBean.storePhotos(user.getId(), album.getId(), Collections.singletonList(multipartFile));
                Photo photo = albumBean.getPhotos(album.getId(), 0, 1).get(0);
                user.setPhotoURL(photo.getPhotoUrl());
            } else {
                user.setPhotoURL(Constants.DEFAULT_PHOTO);
            }
            user = userRepository.save(user);
            EmailHelper.sendMail(user.getEmail(), "Registration", "Thanks for signing up for Hello From! Please click the link below to confirm your email address.\n"
                    + baseUrl + "/activate/" + user.getActivationToken());
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Boolean checkActivationToken(String token) {
        return userRepository.findUserByActivationToken(token) != null;
    }

    @Override
    @Transactional
    public Boolean activateUser(String token, List<String> validationErrors) {
        User user = userRepository.findUserByActivationToken(token);
        if (user != null) {
            if (languageUserRepository.findLanguageUsersByUser_IdOrderByLanguageLevelDesc(user.getId()).isEmpty()) {
                validationErrors.add("User doesn't have any languages");
                return false;
            }
            user.setActivationToken(null);
            user.setConfirmed(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean activateUser(String token, LanguageRegistrationDto languageRegistrationDto, List<String> validationErrors) {
        User user = userRepository.findUserByActivationToken(token);
        if (user != null) {
            Map<Language, LanguageLevel> languageLanguageLevelMap = validateLanguageDtoAndFillLanguageMap(languageRegistrationDto, validationErrors);
            if (!languageLanguageLevelMap.isEmpty() && validationErrors.isEmpty()) {
                List<LanguageUser> languageUsers = new ArrayList<>();
                for (Map.Entry<Language, LanguageLevel> entry : languageLanguageLevelMap.entrySet()) {
                    languageUsers.add(new LanguageUser(user, entry.getKey(), entry.getValue()));
                }
                languageUserRepository.save(languageUsers);
                user.setActivationToken(null);
                user.setConfirmed(true);
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public User updateUser(Long userId, UserRegistrationDto userRegistrationDto, List<String> errors) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            //I think it never happened but who knows...
            errors.add("User with id " + userId + " not found");
            return null;
        }
        if (userRegistrationDto.getPassword() != null && userRegistrationDto.getPassword().trim().length() == 0) {
            userRegistrationDto.setPassword(null);
        }
        if (userRegistrationDto.getOldpassword() != null && userRegistrationDto.getOldpassword().trim().length() == 0) {
            userRegistrationDto.setOldpassword(null);
        }
        if (!validateUserDto(userRegistrationDto, ValidationMode.EDIT, errors)) {
            return null;
        }
        replaceSpecialSymbols(userRegistrationDto);
        if (!user.getLogin().equals(userRegistrationDto.getLogin()) && userRepository.findUserByLogin(userRegistrationDto.getLogin()) != null) {
            errors.add("Login already in use");
        }
        if (userRegistrationDto.getPassword() != null && !passwordEncoder.encode(userRegistrationDto.getOldpassword().trim()).equals(user.getPassword())) {
            errors.add("Incorrect password");
            return null;
        }
        Map<Language, LanguageLevel> languageLanguageLevelMap = validateLanguageDtoAndFillLanguageMap(
                new LanguageRegistrationDto(userRegistrationDto.getLanguages(), userRegistrationDto.getLanguageLevels()), errors);
        if (languageLanguageLevelMap.isEmpty()) {
            errors.add("At least one pair Language - Language Level should exists");
        }
        Country country = listBean.getCountry(userRegistrationDto.getCountry());
        City city = listBean.getCity(userRegistrationDto.getCity());
        Gender gender = listBean.getGender(userRegistrationDto.getGender());
        if (country == null) {
            errors.add("Country with id: " + userRegistrationDto.getCountry() + " doesn't exist");
        }
        if (city == null) {
            errors.add("City with id: " + userRegistrationDto.getCity() + " doesn't exist");
        }
        if (gender == null) {
            errors.add("Gender with id: " + userRegistrationDto.getGender() + " doesn't exist");
        }
        if (!errors.isEmpty()) {
            return null;
        }
        user.setLogin(userRegistrationDto.getLogin());
        if (userRegistrationDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        }
        user.setEmail(userRegistrationDto.getEmail());
        try {
            user.setBirthday(FORMAT.parse(userRegistrationDto.getBirthday()));
        } catch (ParseException ex) {
            //it never happens
        }
        user.setName(userRegistrationDto.getName());
        user.setDescription(userRegistrationDto.getDescription());
        user.setCountry(country);
        user.setCity(city);
        user.setGender(gender);
        List<LanguageUser> languageUsers = languageUserRepository.findLanguageUsersByUser_IdOrderByLanguageLevelDesc(user.getId());
        List<LanguageUser> languageUsersToRemove = new ArrayList<>();
        for (LanguageUser languageUser : languageUsers) {
            if (!languageLanguageLevelMap.containsKey(languageUser.getLanguage())) {
                languageUsersToRemove.add(languageUser);
            } else {
                languageUser.setLanguage(languageUser.getLanguage());
                languageUser.setLanguageLevel(languageLanguageLevelMap.get(languageUser.getLanguage()));
                languageLanguageLevelMap.remove(languageUser.getLanguage()); // this language is updated
            }
        }
        languageUsers.removeAll(languageUsersToRemove);
        for (Map.Entry<Language, LanguageLevel> entry : languageLanguageLevelMap.entrySet()) {
            languageUsers.add(new LanguageUser(user, entry.getKey(), entry.getValue()));
        }
        languageUserRepository.delete(languageUsersToRemove);
        languageUserRepository.save(languageUsers);
        userRepository.save(user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.delete(userId);
    }

    @Override
    public User getUser(Long userId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            return null;
        }
        Album album = albumBean.getAlbum(user.getId(), "Main");
        user.setAlbums(Collections.singletonList(album));
        user.setLanguageUsers(languageUserRepository.findLanguageUsersByUser_IdOrderByLanguageLevelDesc(user.getId()));
        return user;
    }

    @Override
    public User getUser(String login) {
        return userRepository.findUserByLogin(login);
    }

    @Override
    public User getUserByToken(String token) {
        OAuth2Authentication authentication = tokenStore.readAuthentication(token);
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        return userRepository.findUserByLogin(user.getUsername());
    }

    @Override
    @Transactional
    public void uploadNewProfilePhoto(Long userId, MultipartFile multipartFile) {
        User user = userRepository.findOne(userId);
        if (user != null) {
            Album album = albumBean.getAlbum(userId, "Main");
            albumBean.storePhotos(userId, album.getId(), Collections.singletonList(multipartFile));
            Photo photo = albumBean.getPhotos(album.getId(), 0, 1).get(0);
            user.setPhotoURL(photo.getPhotoUrl());
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void setExistsPhotoAsProfilePhoto(Long userId, Long photoId) {
        User user = userRepository.findOne(userId);
        if (user != null) {
            Photo photo = albumBean.getPhoto(photoId);
            if (photo != null) {
                if (!photo.getPhotoUrl().equals(user.getPhotoURL())) {
                    Album album = albumBean.getAlbum(userId, "Main");
                    albumBean.copyPhotoToAlbum(userId, photoId, album.getId());
                    user.setPhotoURL(photo.getPhotoUrl());
                    userRepository.save(user);
                } else {
                    throw new ValidationException("Old and new main photo urls are equal");
                }
            } else {
                throw new ObjectsNotFoundException("Photo with id: " + photoId + " doesn't exists");
            }
        } else {
            throw new ObjectsNotFoundException("User with id: " + userId + " doesn't exists");
        }
    }

    @Override
    public FriendRequest getFriendRequest(Long firstUserId, Long secondUserId) {
        return friendRequestRepository.findFriendRequestByTwoUserIds(firstUserId, secondUserId);
    }

    @Override
    @Transactional
    public Boolean requestResetPassword(String baseUrl, String email) {
        User user = userRepository.findUserByEmailAndConfirmedIsTrue(email);
        if (user != null) {
            user.setResetPasswordToken(UUID.randomUUID().toString());
            user = userRepository.save(user);
            EmailHelper.sendMail(user.getEmail(), "Reset password", "We have received a password reset request for your Hello From account (hopefully by you).\n" +
                    "Your login: " + user.getLogin() + "\n" +
                    "Please click the link below to reset your password.\n" +
                    baseUrl + "/reset/" + user.getResetPasswordToken() + "\nIf you didn't send password reset request, ignore this message.");
            return true;
        }
        return false;
    }

    @Override
    public Boolean checkResetPasswordToken(String token) {
        return userRepository.findUserByResetPasswordTokenAndConfirmedIsTrue(token) != null;
    }

    @Override
    @Transactional
    public Boolean resetPassword(String token, String password, List<String> errors) {
        User user = userRepository.findUserByResetPasswordTokenAndConfirmedIsTrue(token);
        if (user != null) {
            if (password == null || password.trim().length() < 6 || password.trim().length() > 64) {
                errors.add("Password length should be from 6 to 64 symbols");
                return false;
            } else {
                user.setResetPasswordToken(null);
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return true;
            }
        } else {
            errors.add("Incorrect token");
            return false;
        }
    }

    @Override
    @Transactional
    public void switchUserStatus(Long userId, Boolean online) {
        User user = userRepository.findOne(userId);
        user.setOnline(online);
        userRepository.save(user);
    }

    @Override
    public List<User> getUsers(Long userId, Map<String, Object> params, Integer page, Integer count) {
        List<User> users = userRepository.findUsersByCustomFilter(userId, params, page, count);
        for (User user : users) {
            Album album = albumBean.getAlbum(user.getId(), "Main");
            album.setPhotos(albumBean.getPhotos(album.getId(), 0, 1));
            List<Album> albums = new ArrayList<>();
            albums.add(album);
            user.setAlbums(albums);
            user.setLanguageUsers(languageUserRepository.findLanguageUsersByUser_IdOrderByLanguageLevelDesc(user.getId()));
        }
        return users;
    }

    @Override
    public List<User> getUsers(Long dialogId) {
        return userRepository.findUsersByDialog_Id(dialogId);
    }

    @Override
    @Transactional
    public Boolean sendFriendRequest(Long senderId, Long receiverId) {
        FriendRequest friendRequest = friendRequestRepository.findFriendRequestBySender_IdAndReceiver_Id(receiverId, senderId);
        if (friendRequest != null) {
            updateFriendRequest(receiverId, senderId);
            return false;
        } else {
            friendRequest = friendRequestRepository.findFriendRequestBySender_IdAndReceiver_Id(senderId, receiverId);
            if (friendRequest == null) {
                User sender = userRepository.findOne(senderId);
                User receiver = userRepository.findOne(receiverId);
                friendRequest = new FriendRequest(sender, receiver, false);
                friendRequest = friendRequestRepository.save(friendRequest);
                publisher.publishEvent(new FriendEvent(friendRequest));
            }
            return true;
        }
    }

    @Override
    @Transactional
    public Boolean updateFriendRequest(Long senderId, Long receiverId) {
        FriendRequest friendRequest = friendRequestRepository.findFriendRequestBySender_IdAndReceiver_Id(senderId, receiverId);
        if (friendRequest != null) {
            friendRequest.setConfirmed(true);
            friendRequestRepository.save(friendRequest);
            return true;
        } else {
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean deleteFriendRequest(Long firstUserId, Long secondUserId) {
        FriendRequest friendRequest = friendRequestRepository.findFriendRequestByTwoUserIds(firstUserId, secondUserId);
        if (friendRequest != null) {
            friendRequestRepository.delete(friendRequest);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Long countFriendRequest(Long userId) {
        return friendRequestRepository.countFriendRequestsByReceiver_Id(userId);
    }

    @Override
    public Long countFriends(Long userId) {
        return friendRequestRepository.countFriendsByUser_Id(userId);
    }

    private Boolean validateUserDto(UserRegistrationDto userRegistrationDto, ValidationMode validationMode, List<String> validationErrors) {
        if (userRegistrationDto.getLogin() == null) {
            validationErrors.add("Login can't be empty");
        }
        if (userRegistrationDto.getEmail() == null) {
            validationErrors.add("Email can't be empty");
        } else {
            if (!EMAIL_VALIDATOR.isValid(userRegistrationDto.getEmail())) {
                validationErrors.add("Email is incorrect");
            }
        }
        if (userRegistrationDto.getName() == null) {
            validationErrors.add("Name can't be empty");
        }
        if (userRegistrationDto.getBirthday() == null) {
            validationErrors.add("Birthday can't be empty");
        } else {
            try {
                FORMAT.parse(userRegistrationDto.getBirthday());
            } catch (ParseException e) {
                validationErrors.add("Birthday is incorrect format");
            }
        }
        if (userRegistrationDto.getCity() == null) {
            validationErrors.add("City can't be empty");
        }
        if (userRegistrationDto.getCountry() == null) {
            validationErrors.add("Country can't be empty");
        }
        if (userRegistrationDto.getGender() == null) {
            validationErrors.add("Gender can't be empty");
        }
        if (validationMode.equals(ValidationMode.CREATE)) {
            if (userRegistrationDto.getPassword() == null) {
                validationErrors.add("Password can't be null");
            } else {
                if (userRegistrationDto.getPassword().trim().length() < 6 || userRegistrationDto.getPassword().trim().length() > 64) {
                    validationErrors.add("Password has incorrect length");
                }
            }
        } else if (validationMode.equals(ValidationMode.EDIT)) {
            if (userRegistrationDto.getLanguages().isEmpty() || userRegistrationDto.getLanguageLevels().isEmpty()) {
                validationErrors.add("At least one pair Language - Language Level should exists");
            } else {
                if (userRegistrationDto.getLanguages().size() != userRegistrationDto.getLanguageLevels().size()) {
                    validationErrors.add("Incorrect amount of pairs Language - Language Level");
                }
            }
            if (userRegistrationDto.getPassword() != null && userRegistrationDto.getOldpassword() == null) {
                validationErrors.add("Incorrect password");
            } else {
                if ((userRegistrationDto.getPassword() != null) && (userRegistrationDto.getOldpassword() != null) && (userRegistrationDto.getPassword().trim().length() < 6 || userRegistrationDto.getPassword().trim().length() > 64)) {
                    validationErrors.add("Password has incorrect length");
                }
            }
        }
        return validationErrors.isEmpty();
    }

    private Map<Language, LanguageLevel> validateLanguageDtoAndFillLanguageMap(LanguageRegistrationDto languageRegistrationDto, List<String> validationErrors) {
        Map<Language, LanguageLevel> languageLanguageLevelMap = new HashMap<>();
        if (languageRegistrationDto.getLanguages().isEmpty() || languageRegistrationDto.getLanguageLevels().isEmpty()) {
            validationErrors.add("At least one pair Language - Language Level should exists");
        } else {
            if (languageRegistrationDto.getLanguages().size() != languageRegistrationDto.getLanguageLevels().size()) {
                validationErrors.add("Incorrect amount of pairs Language - Language Level");
            } else {
                for (int i = 0; i < languageRegistrationDto.getLanguages().size(); i++) {
                    if (languageRegistrationDto.getLanguages().get(i) != null && languageRegistrationDto.getLanguageLevels().get(i) != null) {
                        Language language = listBean.getLanguage(languageRegistrationDto.getLanguages().get(i));
                        LanguageLevel languageLevel = listBean.getLanguageLevel(languageRegistrationDto.getLanguageLevels().get(i));
                        if (language != null && languageLevel != null) {
                            languageLanguageLevelMap.put(language, languageLevel);
                        } else {
                            if (language == null) {
                                validationErrors.add("Language with id: " + languageRegistrationDto.getLanguages().get(i) + " doesn't exists");
                            }
                            if (languageLevel == null) {
                                validationErrors.add("Language Level with id: " + languageRegistrationDto.getLanguageLevels().get(i) + " doesn't exists");
                            }
                        }

                    }
                }
            }
        }
        if (validationErrors.isEmpty()) {
            return languageLanguageLevelMap;
        } else {
            return new HashMap<>();
        }

    }

    private void replaceSpecialSymbols(UserRegistrationDto userRegistrationDto) {
        userRegistrationDto.setLogin(StringHelper.replaceSymbols(userRegistrationDto.getLogin().trim()));
        userRegistrationDto.setName(StringHelper.replaceSymbols(userRegistrationDto.getName().trim()));
        if (userRegistrationDto.getDescription() != null) {
            userRegistrationDto.setDescription(StringHelper.replaceSymbols(userRegistrationDto.getDescription().trim()));
        }
    }
}
