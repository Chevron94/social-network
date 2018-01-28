package socialnetwork.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import socialnetwork.beans.UserBean;
import socialnetwork.beans.ListBean;
import socialnetwork.dto.UserDto;
import socialnetwork.entities.User;
import socialnetwork.helpers.UrlConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by roman on 10/6/15.
 */
@Controller
public class PeopleController extends GenericController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleController.class);
    @Autowired
    private ListBean listBean;
    @Autowired
    private UserBean userBean;

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public String friends(Model model, HttpServletRequest request) {
        Long idRequestUser = getUserId(request);
        model.addAttribute("continents", listBean.getContinents());
        model.addAttribute("languages", listBean.getLanguages());
        model.addAttribute("idRequestUser", idRequestUser);
        return "friends";
    }

    @RequestMapping(value = "/user{id}/friends", method = RequestMethod.GET)
    public String usersFriends(Model model, HttpServletRequest request, @PathVariable String id) {
        getUserId(request);
        model.addAttribute("continents", listBean.getContinents());
        model.addAttribute("languages", listBean.getLanguages());
        model.addAttribute("idRequestUser", Long.valueOf(id));
        return "friends";
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String users(Model model, HttpServletRequest request) {
        Long idRequestUser = getUserId(request);
        model.addAttribute("continents", listBean.getContinents());
        model.addAttribute("languages", listBean.getLanguages());
        model.addAttribute("idRequestUser", idRequestUser);
        return "users";
    }


    /**
     * КОНТРОЛЛЕРЫ ЗАПРОСОВ
     **/
    @RequestMapping(value = "/people/sendRequest", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity sendFriendRequest(
            @RequestParam(value = "idReceiver") Long idReceiver,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(userBean.sendFriendRequest(userId, idReceiver));

    }

    @RequestMapping(value = "/people/confirmRequest", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity confirmRequest(
            @RequestParam(value = "idSender") Long idSender,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(userBean.updateFriendRequest(idSender, userId));
    }

    @RequestMapping(value = "/people/deleteRequest", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity deleteRequest(
            @RequestParam(value = "idReceiver") Long idReceiver,
            HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(userBean.deleteFriendRequest(userId, idReceiver));
    }

    @RequestMapping(value = "/people/more", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity loadMoreFriends(
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
            @RequestParam(value = "start") Integer start
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
        Long idUser = getUserId(request);
        if ((list.equals("sent") || list.equals("received")) && !Objects.equals(idRequestUser, idUser)) {
            return null;
        }
        List<User> users = userBean.getUsers(idRequestUser, params, start, 20);
        List<UserDto> result = new ArrayList<>();
        for (User user : users) {
            user.setPhotoURL(UrlConstructor.constructUrl(request.getRequestURL().toString(), user.getPhotoURL()));
            result.add(new UserDto(user));
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/people/requests", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity getCountOfFriendRequests(HttpServletRequest request) {
        Long idUser = getUserId(request);
        if (idUser == null)
            return null;
        return ResponseEntity.ok(userBean.countFriendRequest(idUser));
    }
}
