package socialnetwork.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import socialnetwork.beans.UserBean;
import socialnetwork.entities.Photo;
import socialnetwork.entities.User;
import socialnetwork.helpers.UrlConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Роман on 02.10.2016.
 */
@Controller
public class GenericController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericController.class);
    @Autowired
    private UserBean userBean;

    Long getUserId(HttpServletRequest request) {
        Long idUser = (Long) request.getSession().getAttribute("idUser");
        if (idUser == null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = userBean.getUser(auth.getName());
            if (user != null) {
                request.getSession().setAttribute("idUser", user.getId());
                idUser = user.getId();
            }
        }
        return idUser;
    }

    void constructUrl(String requestUrl, List<Photo> photos){
        for (Photo photo: photos) {
            photo.setPhotoUrl(UrlConstructor.constructUrl(requestUrl, photo.getPhotoUrl()));
        }
    }
}
