package socialnetwork.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socialnetwork.beans.GraphicsBean;
import socialnetwork.beans.ListBean;
import socialnetwork.entities.User;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Roman on 18.04.2018 21:05.
 */
@Controller
@RequestMapping(value = "/graphics")
public class GraphicsController extends GenericController {
    @Autowired
    private ListBean listBean;
    @Autowired
    private GraphicsBean graphicsBean;

    @RequestMapping(method = RequestMethod.GET)
    public String friends(Model model, HttpServletRequest request) {
        getUserId(request);
        model.addAttribute("countries", listBean.getCountries());
        return "graphics";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users-by-country")
    public ResponseEntity usersByCountryGraph() {
        return ResponseEntity.ok(graphicsBean.getUsersByCountryDiagram());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/friends-by-specific-country/{countryID}")
    public ResponseEntity usersByCountryGraph(@PathVariable("countryID") Long countryID) {
        return ResponseEntity.ok(graphicsBean.getFriendsByCountryPerSpecificCountryDiagram(countryID));
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user-friends-by-country")
    public ResponseEntity userFriendsByCountryGraph(HttpServletRequest request) {
        Long userID = getUserId(request);
        return ResponseEntity.ok(graphicsBean.getFriendsByCountryPerUserDiagram(userID));
    }
}
