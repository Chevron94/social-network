package socialnetwork.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import socialnetwork.beans.VocabularyBean;
import socialnetwork.entities.Country;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by roman on 9/26/15.
 */
@Controller
public class MainController{
    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private VocabularyBean vocabularyBean;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(Model model, HttpServletRequest request, HttpServletResponse response) {
        Long idUser = (Long) request.getSession().getAttribute("idUser");
        if(idUser!=null){
            return "redirect:/profile";
        }
        List<Country> countries = vocabularyBean.getCountriesWithUsers();
        model.addAttribute("countries",countries);
        return "main";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView model = new ModelAndView();
        if (error != null) {
            model.addObject("error", "Invalid username and password!");
        }

        if (logout != null) {
            model.addObject("msg", "You've been logged out successfully.");
        }
        String success = (String)request.getSession().getAttribute("msg");
        if (success != null) {
            model.addObject("msg", success);
            request.getSession().removeAttribute("msg");
        }
        String err = (String) request.getSession().getAttribute("error");
        if (err != null){
            model.addObject("error",err);
            request.getSession().removeAttribute("error");
        }
        model.setViewName("login");
        return model;
    }

    @RequestMapping(value = "/404", method = RequestMethod.GET)
    public String error404() {
        return "404";
    }

}
