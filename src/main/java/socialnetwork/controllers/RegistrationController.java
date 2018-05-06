package socialnetwork.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import socialnetwork.beans.CapchaBean;
import socialnetwork.beans.UserBean;
import socialnetwork.beans.ListBean;
import socialnetwork.dto.creation.LanguageRegistrationDto;
import socialnetwork.dto.creation.UserRegistrationDto;
import socialnetwork.entities.Language;
import socialnetwork.entities.LanguageLevel;
import socialnetwork.exceptions.ValidationException;
import socialnetwork.helpers.UrlConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 10/7/15.
 */
@Controller
public class RegistrationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private ListBean listBean;
    @Autowired
    private UserBean userBean;

    @Autowired
    private CapchaBean capchaBean;

    @RequestMapping(value = "/registration/citiesByCountry", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getCities(
            @RequestParam(value = "searchId") Long searchId,
            @RequestParam(value = "name") String name) {

        return ResponseEntity.ok(listBean.getCities(searchId, name));
    }

    @RequestMapping(value = "/registration/countriesByContinent", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getCountries(
            @RequestParam(value = "searchId") Long searchId) {

        return ResponseEntity.ok(listBean.getCountries(searchId));
    }

    @RequestMapping(value = "/registration/checkLogin", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity checkUserLogin(
            @RequestParam(value = "login") String login) {
        return ResponseEntity.ok(userBean.checkLogin(login));
    }

    @RequestMapping(value = "/registration/checkEmail", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity checkUserEmail(
            @RequestParam(value = "email") String email) {
        return ResponseEntity.ok(userBean.checkEmail(email));
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String getRegistration(Model model) {
        model.addAttribute("countries", listBean.getCountries());
        model.addAttribute("genders", listBean.getGenders());
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String setRegistration(@ModelAttribute("user") @Valid UserRegistrationDto user,
                                  BindingResult bindingResult,
                                  Model model,
                                  HttpServletRequest request,
                                  @RequestParam("filePhoto") MultipartFile file) {
        List<String> validationErrors = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                validationErrors.add(error.getDefaultMessage());
            }
        }
        try {
            String capcha = request.getParameter("g-recaptcha-response");
            if (capcha == null || !capchaBean.processResponse(capcha)) {
                validationErrors.add("Capcha is incorrect");
            } else {
                userBean.registerUser(user, file, UrlConstructor.getCurrentUrl(request.getRequestURL().toString()), validationErrors);
            }
        } catch (ValidationException ex) {
            validationErrors.add("File is not an image");
            model.addAttribute("countries", listBean.getCountries());
            model.addAttribute("genders", listBean.getGenders());
            model.addAttribute("user", user);
            if (user.getCountry() != 0 && user.getCity() != 0)
                model.addAttribute("city", listBean.getCity(user.getCity()));
            return "creation";
        }

        if (validationErrors.size() > 0) {
            model.addAttribute("countries", listBean.getCountries());
            model.addAttribute("genders", listBean.getGenders());
            model.addAttribute("user", user);
            model.addAttribute("errors", validationErrors);
            if (user.getCountry() != 0 && user.getCity() != 0)
                model.addAttribute("city", listBean.getCity(user.getCity()));
            return "creation";
        }
        request.getSession().setAttribute("msg", "Please, check your email for instructions");
        return "redirect:/login";
    }

    @RequestMapping(value = "/activate/{token}", method = RequestMethod.GET)
    public String setLanguages(@PathVariable("token") String token, HttpServletRequest request, Model model) {
        if (userBean.checkActivationToken(token)) {
            List<Language> languages = listBean.getLanguages();
            List<LanguageLevel> languageLevels = listBean.getLanguageLevels();
            model.addAttribute("languages", languages);
            model.addAttribute("languageLevels", languageLevels);
            return "completeRegistration";
        } else {
            request.getSession().setAttribute("error", "Incorrect token");
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/activate/{token}", method = RequestMethod.POST)
    public String activateUser(@PathVariable("token") String token, @ModelAttribute("languagesForm") LanguageRegistrationDto languageRegistrationDto, HttpServletRequest request, Model model) {
        List<String> validationErrors = new ArrayList<>();
        if (userBean.activateUser(token, languageRegistrationDto, validationErrors)) {
            if (!validationErrors.isEmpty()) {
                model.addAttribute("error", validationErrors.toString());
                return "completeRegistration";
            }
            request.getSession().setAttribute("msg", "Your account is active. You can login now");
            return "redirect:/login";
        } else {
            request.getSession().setAttribute("error", "Incorrect token");
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/resetRequest", method = RequestMethod.POST)
    public String checkPasswordReset(HttpServletRequest request) {
        String email = request.getParameter("emailInput").trim();
        if (!userBean.requestResetPassword(UrlConstructor.getCurrentUrl(request.getRequestURL().toString()), email)) {
            request.getSession().setAttribute("error", "Email not found or user is not active");
            return "redirect:/login";
        }
        request.getSession().setAttribute("msg", "Please, check your email for instructions");
        return "redirect:/login";
    }

    @RequestMapping(value = "/reset/{token}", method = RequestMethod.GET)
    public String resetPassword(@PathVariable("token") String token, HttpServletRequest request) {
        if (userBean.checkResetPasswordToken(token)) {
            return "passwordReset";
        } else {
            request.getSession().setAttribute("error", "Incorrect token");
            return "redirect:/login";
        }
    }

    @RequestMapping(value = "/reset/{token}", method = RequestMethod.POST)
    public String updatePassword(@PathVariable("token") String token, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        if (userBean.resetPassword(token, request.getParameter("password"), errors)) {
            request.getSession().setAttribute("msg", "Your password was changed. You can login now");
            return "redirect:/login";
        } else {
            request.getSession().setAttribute("error", errors.toString());
            return "redirect:/login";
        }
    }
}
