package socialnetwork.sockets.online;

/**
 * Created by Роман on 25.09.2016.
 */
public class OnlineMessageDTO {
    private Long id;
    private Boolean online;

    public OnlineMessageDTO(Long id, Boolean online) {
        this.id = id;
        this.online = online;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }
}
