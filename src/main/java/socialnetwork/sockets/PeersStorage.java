package socialnetwork.sockets;

import javax.websocket.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Роман on 25.09.2016.
 */
public class PeersStorage {
    private static Map<Long,Sessions> peers = Collections.synchronizedMap(new HashMap<Long, Sessions>());

    public static Map<Long, Sessions> getPeers() {
        return peers;
    }

    public static Long getUserIdBySession(Session session) {
        for (Map.Entry<Long,Sessions> peer : peers.entrySet()) {
            if (peer.getValue().isCurrent(session)) {
                return peer.getKey();
            }
        }
        return null;
    }

    public static void remove(Long id) {
        peers.remove(id);
    }
}
