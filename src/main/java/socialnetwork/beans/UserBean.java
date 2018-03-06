package socialnetwork.beans;

import org.springframework.web.multipart.MultipartFile;
import socialnetwork.dto.creation.LanguageRegistrationDto;
import socialnetwork.dto.creation.UserRegistrationDto;
import socialnetwork.entities.FriendRequest;
import socialnetwork.entities.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Roman on 19.08.2017 13:38.
 */
public interface UserBean {
    User registerUser(UserRegistrationDto userRegistrationDto, MultipartFile multipartFile, String baseUrl, List<String> errors);
    Boolean checkActivationToken(String token);
    Boolean activateUser(String token, List<String> validationErrors);
    Boolean activateUser(String token, LanguageRegistrationDto languageRegistrationDto, List<String> validationErrors);
    User updateUser(Long userId, UserRegistrationDto userRegistrationDto, List<String> errors);
    User getUser(Long userId);
    User getUser(String login);
    User getUserByToken(String token);
    Boolean checkLogin(String login);
    Boolean checkEmail(String email);
    void uploadNewProfilePhoto(Long userId, MultipartFile multipartFile);
    void setExistsPhotoAsProfilePhoto(Long userId, Long photoId);
    FriendRequest getFriendRequest(Long firstUserId, Long secondUserId);
    Boolean requestResetPassword(String baseUrl, String email);
    Boolean checkResetPasswordToken(String token);
    Boolean resetPassword(String token, String password, List<String> errors);
    void switchUserStatus(Long userId, Boolean online);
    List<User> getUsers(Long userId, Map<String, Object> params, Integer page, Integer count);
    List<User> getUsers(Long dialogId);
    Boolean sendFriendRequest(Long senderId, Long receiverId);
    Boolean updateFriendRequest(Long senderId, Long receiverId);
    Boolean deleteFriendRequest(Long firstUserId, Long secondUserId);
    Long countFriendRequest(Long userId);
    Long countFriends(Long userId);

}
