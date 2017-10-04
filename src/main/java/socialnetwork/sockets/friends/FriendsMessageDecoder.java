package socialnetwork.sockets.friends;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

/**
 * Created by Роман on 27.09.2016.
 */
public class FriendsMessageDecoder implements Decoder.Text<FriendsDTO> {
    @Override
    public FriendsDTO decode(String s) throws DecodeException {
        //TODO Implements notifications about new friend requests
        //JsonObject obj = Json.createReader(new StringReader(s))
         //       .readObject();
        FriendsDTO friendsDTO = new FriendsDTO();
       // friendsDTO.setSender(Long.valueOf(obj.getString("sender")));
       // friendsDTO.setReceiver(Long.valueOf(obj.getString("receiver")));
       // friendsDTO.setAccept(obj.getBoolean("accept"));
        return friendsDTO;
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
