package socialnetwork.events;

import socialnetwork.entities.Message;

/**
 * Created by Роман on 14.03.2016.
 */
public class MessageEvent {
    private Message message;

    public MessageEvent() {
    }

    public MessageEvent(Message message) {
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
