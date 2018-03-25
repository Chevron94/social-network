package socialnetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by roman on 14.09.15.
 */
@Entity
@Table(name = "MESSAGE")
public class Message {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DIALOG_ID", nullable = false)
    private Dialog dialog;

    @Column(name = "TEXT")
    private String text;

    @Column(name = "DATETIME")
    private Date dateTime;

    @Column(name = "IS_READ")
    private boolean read;



    @OneToMany(fetch = FetchType.LAZY, mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<File> files = new ArrayList<>();



    public Message() {
    }

    public Message(User from, Dialog to, String text, Date dateTime, boolean isRead) {
        this.user = from;
        this.dialog = to;
        this.text = text;
        this.dateTime = dateTime;
        this.read = isRead;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean getRead() {
        return read;
    }

    public void setRead(boolean isRead) {
        this.read = isRead;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        return !(id != null ? !id.equals(message.id) : message.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", user=" + user +
                ", dialog=" + dialog +
                ", text='" + text + '\'' +
                ", dateTime=" + dateTime +
                ", read=" + read +
                '}';
    }
}
