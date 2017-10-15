package socialnetwork.sockets.dialog;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import socialnetwork.app.WebSocketsConfiguration;
import socialnetwork.beans.DialogBean;
import socialnetwork.beans.UserBean;
import socialnetwork.entities.Dialog;
import socialnetwork.entities.Message;
import socialnetwork.entities.User;
import socialnetwork.events.MessageEvent;
import socialnetwork.events.ReadEvent;
import socialnetwork.sockets.PeersStorage;
import socialnetwork.sockets.Sessions;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 10/4/15.
 */
@Component
@ServerEndpoint(value = "/sockets/dialog", configurator = WebSocketsConfiguration.class, encoders = {DialogMessageEncoder.class}, decoders = {DialogMessageDecoder.class})
public class DialogEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(DialogEndpoint.class);

    @Autowired
    private UserBean userBean;

    @Autowired
    private DialogBean dialogBean;

    @OnOpen
    public void open(final Session session) {

    }

    @OnMessage
    public void onMessage(final Session session, final MessageDto chatMessage) {
        try {
            if (chatMessage == null) {
                return;
            }
            if (chatMessage.getMessageText() == null  && chatMessage.getDialogId() == null) {
                Sessions sessions = PeersStorage.getPeers().get(chatMessage.getSenderId());
                if (sessions == null) {
                    sessions = new Sessions();
                }
                sessions.setMessageSession(session);
            } else {
                if (chatMessage.getDialogId() != null && dialogBean.getDialog(chatMessage.getSenderId(), Long.valueOf(chatMessage.getDialogId())) != null) {
                    if (chatMessage.getMessageText().equals("")) {
                        dialogBean.readMessages(chatMessage.getSenderId(), chatMessage.getDialogId());
                    } else {
                        dialogBean.createMessage(chatMessage.getSenderId(), chatMessage.getDialogId(), chatMessage.getMessageText());
                    }
                }
            }
        } catch (Exception e) {
           LOGGER.error("Failed, ex: ", e);
        }
    }

    @EventListener
    public void newMessageEvent(MessageEvent messageEvent){
        try {
            Message message = messageEvent.getMessage();
            MessageDto messageDto = new MessageDto(message);
            Dialog d = message.getDialog();
            List<User> users = userBean.getUsers(d.getId());
            List<Session> sessions = new ArrayList<>();
            for (User user: users) {
                if (PeersStorage.getPeers().get(user.getId()) != null && PeersStorage.getPeers().get(user.getId()).getMessageSession() != null) {
                    sessions.add(PeersStorage.getPeers().get(user.getId()).getMessageSession());
                }
            }
            for (Session s : sessions) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendObject(messageDto);
                }
            }
        } catch (EncodeException | IOException ignored) {

        }
    }

    @EventListener
    public void readEvent(ReadEvent readEvent){
        try {
            MessageDto messageDto = new MessageDto();
            messageDto.setSenderId(readEvent.getIdUser());
            messageDto.setDialogId(readEvent.getIdDialog());
            messageDto.setMessageText("");
            List<User> users = userBean.getUsers(readEvent.getIdDialog());
            List<Session> sessions = new ArrayList<>();
            for (User user: users) {
                if (PeersStorage.getPeers().get(user.getId()) != null && PeersStorage.getPeers().get(user.getId()).getMessageSession() != null) {
                    sessions.add(PeersStorage.getPeers().get(user.getId()).getMessageSession());
                }
            }
            for (Session s : sessions) {
                if (s.isOpen()) {
                    s.getBasicRemote().sendObject(messageDto);
                }
            }
        } catch (EncodeException | IOException ignored) {

        }
    }

    @OnClose
    public void close(final Session session) {

    }
}
