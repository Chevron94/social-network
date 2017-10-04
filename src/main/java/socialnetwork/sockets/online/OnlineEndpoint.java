package socialnetwork.sockets.online;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.standard.SpringConfigurator;
import socialnetwork.app.WebSocketsConfiguration;
import socialnetwork.beans.UserBean;
import socialnetwork.sockets.PeersStorage;
import socialnetwork.sockets.Sessions;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Роман on 25.09.2016.
 */
@Component
@ServerEndpoint(value = "/sockets/online", configurator = WebSocketsConfiguration.class, encoders = {OnlineMessageEncoder.class}, decoders = {OnlineMessageDecoder.class})
public class OnlineEndpoint {
    @Autowired
    private UserBean userBean;

    @OnOpen
    public void open(final Session session) {
    }

    @OnClose
    public void close(final Session session) throws IOException, EncodeException {
        Long userId = PeersStorage.getUserIdBySession(session);
        if (userId != null) {
            userBean.switchUserStatus(userId, false);
            PeersStorage.remove(userId);
            for (Map.Entry<Long, Sessions> entry : PeersStorage.getPeers().entrySet()) {
                if (entry.getValue().getOnlineSession() != null)
                    entry.getValue().getOnlineSession().getBasicRemote().sendObject(new OnlineMessageDTO(userId, false));
            }
        }
    }

    @OnMessage
    public void onMessage(final Session session, final Long id) throws IOException, EncodeException {
        Sessions sessions = PeersStorage.getPeers().get(id);
        if (sessions == null) {
            sessions = new Sessions();
        }
        sessions.setOnlineSession(session);
        userBean.switchUserStatus(id, true);
        PeersStorage.getPeers().put(id, sessions);
        for (Map.Entry<Long, Sessions> entry : PeersStorage.getPeers().entrySet()) {
            if (entry.getValue().getOnlineSession() != null) {
                entry.getValue().getOnlineSession().getBasicRemote().sendObject(new OnlineMessageDTO(id, true));
            }
        }
    }
}
