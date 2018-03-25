package socialnetwork.rest;

/**
 * Created by Roman on 10.02.2018 14:22.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import socialnetwork.beans.UserBean;
import socialnetwork.entities.User;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE;

@Controller
public class GenericAPIController {

    @Autowired
    private UserBean userBean;

    public User getUser(HttpServletRequest request){
        String token = (String) request.getAttribute(ACCESS_TOKEN_VALUE);
        return userBean.getUserByToken(token);
    }
}
