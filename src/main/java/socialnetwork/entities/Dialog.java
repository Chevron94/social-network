package socialnetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by roman on 22.09.15.
 */
@Entity
@Table(name="DIALOG")
public class Dialog {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name="LAST_MESSAGE_DATETIME", nullable = true)
    private Date lastMessageDate;

    @Column(name = "IS_PRIVATE")
    private Boolean isPrivate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dialog", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Message> messages = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dialog", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserDialog> userDialogs = new ArrayList<>();



    public Dialog() {
    }

    public Dialog(String name, Boolean isPrivate) {
        this.name = name; this.isPrivate = isPrivate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<UserDialog> getUserDialogs() {
        return userDialogs;
    }

    public void setUserDialogs(List<UserDialog> userDialogs) {
        this.userDialogs = userDialogs;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dialog dialog = (Dialog) o;

        return !(id != null ? !id.equals(dialog.id) : dialog.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Dialog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
