package socialnetwork.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socialnetwork.beans.AlbumBean;
import socialnetwork.beans.UserBean;
import socialnetwork.beans.ListBean;
import socialnetwork.dto.creation.UserRegistrationDto;
import socialnetwork.entities.Album;
import socialnetwork.entities.FriendRequest;
import socialnetwork.entities.User;
import socialnetwork.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by roman on 10/4/15.
 */
@Controller
public class ProfileController extends GenericController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private ListBean listBean;
    @Autowired
    private UserBean userBean;
    @Autowired
    private AlbumBean albumBean;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getMainProfile(HttpServletRequest request) {
        Long idUser = getUserId(request);
        return "forward:/user" + idUser;
    }

    @RequestMapping(value = "/profile/updatePhoto", method = RequestMethod.POST)
    public String updatePhoto(@RequestParam("photoInput") MultipartFile file, HttpServletRequest request) {
        Long userId = getUserId(request);
        if (!file.isEmpty()) {
            userBean.uploadNewProfilePhoto(userId, file);
        }
        return "redirect:/profile";
    }

    @RequestMapping(value = "/user{id}", method = RequestMethod.GET)
    public String getUserProfile(Model model, HttpServletRequest request, @PathVariable String id) {
        Long idUser = getUserId(request);
        User user = userBean.getUser(Long.valueOf(id));
        if (user != null) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("list", "friends");
            List<User> users = userBean.getUsers(user.getId(), params, 0, 9);
            model.addAttribute("friends", users);
            model.addAttribute("user", user);
            Long idRequestUser = Long.valueOf(id);
            if (!Objects.equals(idRequestUser, idUser)) {
                FriendRequest friendRequest = userBean.getFriendRequest(idUser, idRequestUser);
                model.addAttribute("friendRequest", friendRequest);
            }
            List<Album> albums = albumBean.getAlbums(user.getId(), 0, 3);
            model.addAttribute("albums", albums);
            model.addAttribute("numberOfAlbums", albumBean.countAlbums(user.getId()));
            model.addAttribute("numberOfFriends", userBean.countFriends(user.getId()));
            model.addAttribute("languages", listBean.getLanguages());
            model.addAttribute("languageLevels", listBean.getLanguageLevels());
            model.addAttribute("countries", listBean.getCountries());
            model.addAttribute("genders", listBean.getGenders());
            return "profile";
        }
        return "forward:/404";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String updateProfile(Model model, @ModelAttribute("editForm") UserRegistrationDto userRegistrationDto, HttpServletRequest request) {
        Long userId = getUserId(request);
        List<String> errors = new ArrayList<>();
        User user = userBean.updateUser(userId, userRegistrationDto, errors);
        model.addAttribute("errors", errors);
        return getUserProfile(model, request, user.getId().toString());
    }

    @RequestMapping(value = "/profile/updateMainPhoto", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity updateMainPhoto(@RequestParam("idPhoto") Long idPhoto,
                                   HttpServletRequest request) {
        Long userId = getUserId(request);
        try {
            userBean.setExistsPhotoAsProfilePhoto(userId, idPhoto);
            return ResponseEntity.ok(true);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
