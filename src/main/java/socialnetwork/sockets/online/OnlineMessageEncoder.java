package socialnetwork.sockets.online;

import javax.json.Json;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by Роман on 25.09.2016.
 */
public class OnlineMessageEncoder implements Encoder.Text<OnlineMessageDTO> {
    @Override
    public String encode(OnlineMessageDTO onlineMessageDTO) throws EncodeException {
         return Json.createObjectBuilder()
                .add("userId",onlineMessageDTO.getId())
                .add("online", onlineMessageDTO.getOnline()).build().toString();
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
