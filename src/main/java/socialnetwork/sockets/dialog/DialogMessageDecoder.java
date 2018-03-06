package socialnetwork.sockets.dialog;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by roman on 10/4/15.
 */
public class DialogMessageDecoder implements Decoder.Text<MessageDto> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DialogMessageDecoder.class);

    @Override
    public void init(final EndpointConfig config) {
        //nothing to do is required
    }

    @Override
    public void destroy() {
        //nothing to do is required
    }

    @Override
    public MessageDto decode(final String textMessage) throws DecodeException {
        MessageDto message = new MessageDto();
        String tmp = textMessage.replaceAll("\n", "\\\\n\\\\r");
        JsonObject obj = Json.createReader(new StringReader(tmp))
                .readObject();
        try {
            //connect
            message.setSenderId(Long.valueOf(obj.getString("senderId")));
            //read
            if (obj.containsKey("dialog")) {
                message.setDialogId(Long.valueOf(obj.getString("dialog")));
                //send
                if (obj.containsKey("messageText")) {
                    message.setMessageText(obj.getString("messageText"));
                    message.setMessageText(StringUtils.replaceEach(obj.getString("messageText"), new String[]{"&", "\"", "<", ">", "'", "/",}, new String[]{"&amp;", "&quot;", "&lt;", "&gt;", "&apos;", "&#x2F;"}));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    message.setSender(obj.getString("sender"));
                    Date date = new Date();
                    message.setReceivedDate((date));
                    message.setReceived(simpleDateFormat.format(date));
                }
            }
        } catch (Exception ignored) {
            LOGGER.error("Error, ex: ", ignored);
            return null;
        }
        return message;
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }
}
