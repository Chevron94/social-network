package socialnetwork.rest;

/**
 * Created by Roman on 10.02.2018 14:22.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import socialnetwork.beans.UserBean;
import socialnetwork.entities.User;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE;

@RestController
@RequestMapping(value = "/api/v1/test")
public class BasicAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicAPIController.class);

    @Autowired
    private UserBean userBean;

    @RequestMapping(method = RequestMethod.GET)
    public String test(HttpServletRequest request){
        User user = getUser(request);
        LOGGER.info("User {}", user.getId());
        return "It works";
    }

    public User getUser(HttpServletRequest request){
        String token = (String) request.getAttribute(ACCESS_TOKEN_VALUE);
        return userBean.getUserByToken(token);
    }
}
