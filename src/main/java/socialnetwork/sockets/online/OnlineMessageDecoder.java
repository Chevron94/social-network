package socialnetwork.sockets.online;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

/**
 * Created by Роман on 25.09.2016.
 */
public class OnlineMessageDecoder implements Decoder.Text<Long> {
    @Override
    public Long decode(String s) throws DecodeException {
        return Long.parseLong(s);
    }

    @Override
    public boolean willDecode(String s) {
        return false;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}
