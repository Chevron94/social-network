package socialnetwork.sockets.friends;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import socialnetwork.app.WebSocketsConfiguration;
import socialnetwork.beans.UserBean;
import socialnetwork.entities.FriendRequest;
import socialnetwork.events.FriendEvent;
import socialnetwork.sockets.PeersStorage;
import socialnetwork.sockets.Sessions;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by Роман on 27.09.2016.
 */
@Component
@ServerEndpoint(value = "/sockets/friends", configurator = WebSocketsConfiguration.class, encoders = {FriendsMessageEncoder.class}, decoders = {FriendsMessageDecoder.class})
public class FriendsEndpoint {

    private static final Logger LOGGER = Logger.getLogger(FriendsEndpoint.class);

    @Autowired
    private UserBean userBean;

    @OnOpen
    public void open(final Session session) {
    }

    @OnClose
    public void close(final Session session) {

    }

    @OnMessage
    public void onMessage(final Session session, final FriendsDTO friendsDTO){
        if (friendsDTO.getReceiver() == null) {
            Sessions sessions = PeersStorage.getPeers().get(friendsDTO.getSender());
            if (sessions == null) {
                sessions = new Sessions();
            }
            sessions.setFriendsSession(session);
        } else {
            if (friendsDTO.getAccept()) {
                userBean.sendFriendRequest(friendsDTO.getSender(), friendsDTO.getReceiver());
            } else {
                userBean.deleteFriendRequest(friendsDTO.getSender(), friendsDTO.getReceiver());
            }
        }
    }

    @EventListener
    public void friendRequestEvent(FriendEvent friendRequestEvent) throws IOException, EncodeException {
        FriendRequest friendRequest = friendRequestEvent.getFriendRequest();
        Sessions sessions = PeersStorage.getPeers().get(friendRequest.getReceiver().getId());
        if (sessions!= null && sessions.getFriendsSession() != null) {
            sessions.getFriendsSession().getBasicRemote().sendObject(friendRequest);
        }
    }
}
