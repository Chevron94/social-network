package socialnetwork.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialnetwork.beans.DialogBean;
import socialnetwork.dto.CreateDialogDto;
import socialnetwork.dto.DialogDto;
import socialnetwork.dto.DialogMessageDto;
import socialnetwork.entities.Dialog;
import socialnetwork.entities.Message;
import socialnetwork.entities.User;
import socialnetwork.exceptions.ValidationException;
import socialnetwork.sockets.dialog.MessageDto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 06.03.2018 12:25.
 */
@RestController
@RequestMapping(value = "/api/v1/dialogs")
public class DialogsAPIController extends GenericAPIController{

    @Autowired
    private DialogBean dialogBean;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getDialogs(@RequestParam(value = "start", defaultValue = "0") Integer start,
                              @RequestParam(value = "count", defaultValue = "100") Integer count,
                              HttpServletRequest request) {
        User user = getUser(request);
        try {
            List<Dialog> dialogs = dialogBean.getDialogs(user.getId(), start, count);
            List<DialogDto> result = new ArrayList<>();
            for (Dialog dialog: dialogs) {
                result.add(new DialogDto(dialog));
            }
            return ResponseEntity.ok(result);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity createNewConversation(@RequestBody CreateDialogDto createDialogDto,
                                         HttpServletRequest request) {
        try {
            User user = getUser(request);
            createDialogDto.getUsers().add(user.getId());
            Dialog dialog = dialogBean.createDialog(createDialogDto.getName(), new ArrayList<>(createDialogDto.getUsers()));
            return ResponseEntity.status(HttpStatus.CREATED).body(new DialogDto(dialog));
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/{dialogId}/messages", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getMessages(@PathVariable(value = "dialogId") Long dialogId,
                               @RequestParam(value = "start", defaultValue = "0") Integer start,
                               @RequestParam(value = "count", defaultValue = "100") Integer count,
                               HttpServletRequest request) {
        User user = getUser(request);
        try {
            List<Message> messages = dialogBean.getMessages(user.getId(), dialogId, start, count);
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



    @RequestMapping(value = "/{dialogId}", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getDialog(@PathVariable("dialogId") Long dialogId,
                             HttpServletRequest request) {
        User user = getUser(request);
        try {
            Dialog dialog = dialogBean.getDialog(user.getId(), dialogId);
            if (dialog == null) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.ok(new DialogDto(dialog));
            }
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/messages", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity postMessage(@RequestBody DialogMessageDto dialogMessageDto,
                               HttpServletRequest request) {
        User user = getUser(request);
        try {
            dialogBean.createMessage(user.getId(), dialogMessageDto.getDialogId(), dialogMessageDto.getText());
            return ResponseEntity.status(HttpStatus.CREATED).body(dialogMessageDto);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getDialogsWithCntUnreadMessages(HttpServletRequest request) {
        User user = getUser(request);
        return ResponseEntity.ok(dialogBean.getDialogsWithNumberOfUnreadMessages(user.getId()));
    }


}
