package socialnetwork.events;

/**
 * Created by Роман on 06.04.2016.
 */
public class ReadEvent {
    private Long idUser;
    private Long idDialog;

    public ReadEvent() {
    }

    public ReadEvent(Long idUser, Long idDialog) {
        this.idUser = idUser;
        this.idDialog = idDialog;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdDialog() {
        return idDialog;
    }

    public void setIdDialog(Long idDialog) {
        this.idDialog = idDialog;
    }
}
