package socialnetwork.beans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import socialnetwork.beans.DialogBean;
import socialnetwork.entities.Dialog;
import socialnetwork.entities.Message;
import socialnetwork.entities.User;
import socialnetwork.entities.UserDialog;
import socialnetwork.events.MessageEvent;
import socialnetwork.events.ReadEvent;
import socialnetwork.exceptions.AccessDeniedException;
import socialnetwork.exceptions.ObjectsNotFoundException;
import socialnetwork.exceptions.ValidationException;
import socialnetwork.helpers.StringHelper;
import socialnetwork.repositories.DialogRepository;
import socialnetwork.repositories.MessageRepository;
import socialnetwork.repositories.UserDialogRepository;
import socialnetwork.repositories.UserRepository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Created by Roman on 19.08.2017.
 */
@Component
public class DialogBeanImpl implements DialogBean {
    @Autowired
    private DialogRepository dialogRepository;
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserDialogRepository userDialogRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public Dialog getDialog(Long userId, Long dialogId) {
        validate(userId, dialogId);
        Dialog dialog = dialogRepository.findOne(dialogId);
        formDialogName(userId, dialog);
        return dialog;

    }

    @Override
    public List<Dialog> getDialogs(Long userId, Integer page, Integer count) {
        List<Dialog> dialogs = dialogRepository.findDialogsByUser_Id(userId, new PageRequest(page, count));
        for (Dialog dialog : dialogs) {
            formDialogName(userId, dialog);
        }
        return dialogs;
    }

    @Override
    public Map<Long, Long> getDialogsWithNumberOfUnreadMessages(Long userId) {
        List<Dialog> dialogs = dialogRepository.findDialogsByUser_IdWithUnreadMessages(userId);
        Map<Long, Long> dialogCountOfUnreadMessagesMap = new HashMap<>();
        for (Dialog dialog : dialogs) {
            dialogCountOfUnreadMessagesMap.put(dialog.getId(), messageRepository.countMessagesByUser_IdAndDialog_IdAndReadIsFalse(userId, dialog.getId()));
        }
        return dialogCountOfUnreadMessagesMap;
    }

    @Override
    @Transactional
    public Dialog createDialog(String name, List<Long> userIds) {
        if (userIds.size() < 2) {
            throw new ValidationException("Number of users in dialog should be >= 2");
        }
        if (userIds.size() == 2) {
            Dialog dialog = dialogRepository.findDialogByTwoUsers(userIds.get(0), userIds.get(1));
            if (dialog == null) {
                List<User> users = userRepository.findAll(userIds);
                dialog = new Dialog(users.get(0).getLogin() + " / " + users.get(1).getLogin(), true);
                dialog = dialogRepository.save(dialog);
                List<UserDialog> userDialogs = new ArrayList<>();
                for (User user : users) {
                    userDialogs.add(new UserDialog(dialog, user));
                }
                userDialogRepository.save(userDialogs);
            }
            return dialog;
        } else {
            Dialog dialog = new Dialog(StringHelper.replaceSymbols(name), false);
            dialog = dialogRepository.save(dialog);
            List<User> users = userRepository.findAll(userIds);
            List<UserDialog> userDialogs = new ArrayList<>();
            for (User user : users) {
                userDialogs.add(new UserDialog(dialog, user));
            }
            userDialogRepository.save(userDialogs);
            dialog.setUserDialogs(userDialogs);
            return dialog;
        }
    }

    @Override
    @Transactional
    public void deleteDialog(Long userId, Long dialogId) {
        //todo design and implement
        throw new NotImplementedException();
    }

    @Override
    public List<Message> getMessages(Long userId, Long dialogId, Integer page, Integer size) {
        validate(userId, dialogId);
        List<Message> messages = messageRepository.findMessagesByDialog_IdOrderByDateTimeDesc(dialogId, new PageRequest(page, size));
        Collections.reverse(messages);
        return messages;
    }

    @Override
    @Transactional
    public Message createMessage(Long userId, Long dialogId, String text) {
        validate(userId, dialogId);
        User user = userRepository.findOne(userId);
        Dialog dialog = dialogRepository.findOne(dialogId);
        text = StringHelper.replaceSymbols(text);
        Message message = new Message(user, dialog, text, new Date(), false);
        message = messageRepository.save(message);
        dialog.setLastMessageDate(message.getDateTime());
        dialogRepository.save(dialog);
        MessageEvent messageEvent = new MessageEvent(message);
        publisher.publishEvent(messageEvent);
        return message;
    }

    @Override
    @Transactional
    public Message createDirectMessage(Long senderId, Long receiverId, String text) {
        Dialog dialog = dialogRepository.findDialogByTwoUsers(senderId, receiverId);
        if (dialog == null) {
            dialog = createDialog(null, Arrays.asList(senderId, receiverId));
        }
        return createMessage(senderId, dialog.getId(), text);
    }

    @Override
    @Transactional
    public void deleteMessage(Long userId, Long dialogId) {
        //todo design and implement
        throw new NotImplementedException();
    }

    @Override
    @Transactional
    public void readMessages(Long userId, Long dialogId) {
        validate(userId, dialogId);
        List<Message> messages = messageRepository.findMessagesToRead(userId, dialogId);
        for (Message message: messages) {
            message.setRead(true);
        }
        messageRepository.save(messages);
        publisher.publishEvent(new ReadEvent(userId, dialogId));
    }

    private void validate(Long userId, Long dialogId) {
        List<User> users = userRepository.findUsersByDialog_Id(dialogId);
        User user = userRepository.findOne(userId);
        if (users == null || users.isEmpty()) {
            throw new ObjectsNotFoundException("Dialog with id: " + dialogId + " doesn't exists");
        }
        if (!users.contains(user)) {
            throw new AccessDeniedException("User with id: " + userId + " not in dialog with id: " + dialogId);
        }
    }

    private void formDialogName(Long userId, Dialog dialog) {
        if (dialog.getPrivate()) {
            List<UserDialog> userDialogs = new ArrayList<>();
            userDialogs.add(userDialogRepository.findUserDialogByDialog_IdAndAnotherUser_Id(dialog.getId(), userId));
            dialog.setUserDialogs(userDialogs);
            User user = userRepository.findOne(userId);
            String[] names = dialog.getName().split("/");
            if (names[0].trim().equals(user.getLogin())) {
                dialog.setName(names[1].trim());
            } else dialog.setName(names[0].trim());
        }
    }
}
