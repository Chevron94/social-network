package socialnetwork.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialnetwork.beans.UserBean;
import socialnetwork.dto.UserDto;
import socialnetwork.dto.creation.UserRegistrationDto;
import socialnetwork.entities.User;
import socialnetwork.helpers.UrlConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Roman on 06.03.2018 12:23.
 */
@RestController
@RequestMapping(value = "/api/v1/users")
public class UsersAPIController extends GenericAPIController{

    @Autowired
    private UserBean userBean;

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity loadUsers(
            HttpServletRequest request,
            @RequestParam(value = "idUser") Long idRequestUser,
            @RequestParam(value = "login") String login,
            @RequestParam(value = "idContinent") Long idContinent,
            @RequestParam(value = "idCountry") Long idCountry,
            @RequestParam(value = "idCity") Long idCity,
            @RequestParam(value = "male") Boolean male,
            @RequestParam(value = "female") Boolean female,
            @RequestParam(value = "ageFrom") Integer ageFrom,
            @RequestParam(value = "ageTo") Integer ageTo,
            @RequestParam(value = "idLanguage[]") Long[] idLanguage,
            @RequestParam(value = "list") String list,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "count") Integer count
    ) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("login", login);
        params.put("idContinent", idContinent);
        params.put("idCountry", idCountry);
        params.put("idCity", idCity);
        params.put("male", male);
        params.put("female", female);
        params.put("ageFrom", ageFrom);
        params.put("ageTo", ageTo);
        params.put("idLanguage", idLanguage);
        params.put("list", list);
        User current = getUser(request);
        if ((list.equals("sent") || list.equals("received")) && !Objects.equals(idRequestUser, current.getId())) {
            return null;
        }
        List<User> users = userBean.getUsers(idRequestUser, params, page, count);
        List<UserDto> result = new ArrayList<>();
        for (User user : users) {
            user.setPhotoURL(UrlConstructor.constructUrl(request.getRequestURL().toString(), user.getPhotoURL()));
            result.add(new UserDto(user));
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getUserProfile(@PathVariable("userId") Long userId, HttpServletRequest request){
        User user = userBean.getUser(userId);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User with id " + userId + " doesn't exist");
        } else {
            UserDto userDto = new UserDto(user);
            return ResponseEntity.ok(userDto);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity updateProfile(@RequestBody UserRegistrationDto userRegistrationDto, HttpServletRequest request){
        User user = getUser(request);
        List<String> errors = new ArrayList<>();
        user = userBean.updateUser(user.getId(), userRegistrationDto, errors);
        if (errors.isEmpty()){
            return ResponseEntity.ok(new UserDto(user));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public @ResponseBody
    ResponseEntity deleteUser(HttpServletRequest request){
        User user = getUser(request);
        userBean.deleteUser(user.getId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(value = "/friendRequest", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity sendFriendRequest(
            @RequestParam(value = "idReceiver") Long idReceiver,
            HttpServletRequest request) {
        User user = getUser(request);
        return ResponseEntity.ok(userBean.sendFriendRequest(user.getId(), idReceiver));

    }

    @RequestMapping(value = "/friendRequest", method = RequestMethod.PUT)
    public
    @ResponseBody
    ResponseEntity confirmRequest(
            @RequestParam(value = "idSender") Long idSender,
            HttpServletRequest request) {
        User user = getUser(request);
        return ResponseEntity.ok(userBean.updateFriendRequest(idSender, user.getId()));
    }

    @RequestMapping(value = "/friendRequest", method = RequestMethod.DELETE)
    public
    @ResponseBody
    ResponseEntity deleteRequest(
            @RequestParam(value = "idReceiver") Long idReceiver,
            HttpServletRequest request) {
        User user = getUser(request);
        return ResponseEntity.ok(userBean.deleteFriendRequest(user.getId(), idReceiver));
    }

    //todo change response to include it into GET records methods
    @RequestMapping(value = "/people/requests", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity getCountOfFriendRequests(HttpServletRequest request) {
        User user = getUser(request);
        Long idUser = user.getId();
        if (idUser == null)
            return null;
        return ResponseEntity.ok(userBean.countFriendRequest(idUser));
    }
}
