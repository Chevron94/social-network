package socialnetwork.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import socialnetwork.beans.DialogBean;
import socialnetwork.beans.UserBean;
import socialnetwork.dto.DialogDto;
import socialnetwork.entities.Dialog;
import socialnetwork.entities.Message;
import socialnetwork.entities.User;
import socialnetwork.exceptions.ValidationException;
import socialnetwork.sockets.dialog.MessageDto;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import java.util.*;

/**
 * Created by roman on 10/4/15.
 */
@Controller
public class DialogController extends GenericController {
    private static final Logger LOGGER = Logger.getLogger(DialogController.class);
    @Autowired
    private UserBean userBean;

    @Autowired
    private DialogBean dialogBean;

    @RequestMapping(value = "/dialogs", method = RequestMethod.GET)
    public String getDialog(HttpServletRequest request, Model model) {
        Long idUser = getUserId(request);
        User user = userBean.getUser(idUser);
        model.addAttribute("user", user);
        HashMap<String, Object> params = new HashMap<>();
        params.put("list", "friends");
        List<User> friends = userBean.getUsers(user.getId(), params, 0, Integer.MAX_VALUE);
        model.addAttribute("friends", friends);
        return "dialog";
    }

    @RequestMapping(value = "/dialog/getMessages", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getMessages(@RequestParam(value = "idDialog") Long idDialog, @RequestParam(value = "startMessage") Integer startMessage, HttpServletRequest request) {
        Long userId = (Long) (request.getSession().getAttribute("idUser"));
        try {
            List<Message> messages = dialogBean.getMessages(userId, idDialog, startMessage, 20);
            List<MessageDto> messagesDto = new ArrayList<>();
            for (Message message : messages) {
                MessageDto m = new MessageDto(message);
                messagesDto.add(m);
            }
            return ResponseEntity.ok(messagesDto);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/dialogs/loadMore", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getDialogs(@RequestParam(value = "start") Integer start, HttpServletRequest request) {
        Long idUser = (Long) (request.getSession().getAttribute("idUser"));
        try {
            List<Dialog> dialogs = dialogBean.getDialogs(idUser, start, 10);
            List<DialogDto> result = new ArrayList<>();
            for (Dialog dialog: dialogs) {
                result.add(new DialogDto(dialog));
            }
            return ResponseEntity.ok(result);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/dialogs/{id}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getDialog(@PathVariable("id") Long idDialog, HttpServletRequest request) {
        Long idUser = (Long) (request.getSession().getAttribute("idUser"));
        try {
            Dialog dialog = dialogBean.getDialog(idUser, idDialog);
            if (dialog == null) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.ok(new DialogDto(dialog));
            }
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity getOrCreateDialog(@RequestParam(value = "idReceiver") Long idReceiver, @RequestParam(value = "text") String text, HttpServletRequest request) {
        Long userId = (Long) (request.getSession().getAttribute("idUser"));
        try {
            dialogBean.createDirectMessage(userId, idReceiver, text);
            return ResponseEntity.ok(true);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/unreadMessages", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getDialogsWithCntUnreadMessages(HttpServletRequest request) {
        Long userId = getUserId(request);
        return ResponseEntity.ok(dialogBean.getDialogsWithNumberOfUnreadMessages(userId));
    }

    @RequestMapping(value = "/dialogs", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity createNewConversation(@RequestParam(value = "name") String name,
                                                   @RequestParam("users[]") Long[] users,
                                                   HttpServletRequest request) {
        try {
            Long userId = getUserId(request);
            List<Long> userIdsList = Arrays.asList(users);
            if (!userIdsList.contains(userId)) {
                userIdsList.add(userId);
            }
            Dialog dialog = dialogBean.createDialog(name, userIdsList);
            return ResponseEntity.ok(new DialogDto(dialog));
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
}
