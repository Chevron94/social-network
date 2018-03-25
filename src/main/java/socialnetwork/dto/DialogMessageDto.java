package socialnetwork.dto;

/**
 * Created by Roman on 10.03.2018 12:39.
 */
public class DialogMessageDto {
    private Long dialogId;
    private String text;

    public DialogMessageDto() {
    }

    public DialogMessageDto(Long dialogId, String text) {
        this.dialogId = dialogId;
        this.text = text;
    }

    public Long getDialogId() {
        return dialogId;
    }

    public void setDialogId(Long dialogId) {
        this.dialogId = dialogId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "DialogMessageDto{" +
                "dialogId=" + dialogId +
                ", text='" + text + '\'' +
                '}';
    }
}
