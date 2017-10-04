package socialnetwork.sockets;

import javax.websocket.Session;

/**
 * Created by Роман on 27.09.2016.
 */
public class Sessions {
    private Session messageSession;
    private Session friendsSession;
    private Session onlineSession;

    public Session getMessageSession() {
        return messageSession;
    }

    public void setMessageSession(Session messageSession) {
        this.messageSession = messageSession;
    }

    public Session getFriendsSession() {
        return friendsSession;
    }

    public void setFriendsSession(Session friendsSession) {
        this.friendsSession = friendsSession;
    }

    public Session getOnlineSession() {
        return onlineSession;
    }

    public void setOnlineSession(Session onlineSession) {
        this.onlineSession = onlineSession;
    }

    public Boolean isCurrent(Session session) {
        return onlineSession.equals(session);
    }
}
