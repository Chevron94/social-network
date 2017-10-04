package socialnetwork.beans;

import socialnetwork.entities.Dialog;
import socialnetwork.entities.Message;

import java.util.List;
import java.util.Map;

/**
 * Created by Roman on 19.08.2017.
 */
public interface DialogBean {
    Dialog getDialog(Long userId, Long dialogId);
    List<Dialog> getDialogs(Long userId, Integer page, Integer count);
    Dialog createDialog(String name, List<Long> userIds);
    void deleteDialog(Long userId, Long dialogId);
    List<Message> getMessages(Long userId, Long dialogId, Integer page, Integer size);
    Message createMessage(Long userId, Long dialogId, String text);
    Message createDirectMessage(Long senderId, Long receiverId, String text);
    void deleteMessage(Long userId, Long dialogId);
    void readMessages(Long userId, Long dialogId);

    Map<Long, Long> getDialogsWithNumberOfUnreadMessages(Long userId);

}
