package socialnetwork.entities;

import javax.persistence.*;

/**
 * Created by roman on 22.09.15.
 */
@Entity
@Table(name = "USER_DIALOG")
public class UserDialog {
    @Id
    @Column(name="USER_DIALOG")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DIALOG_ID")
    private Dialog dialog;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID")
    private User user;

    public UserDialog() {
    }

    public UserDialog(Dialog dialog, User user) {
        this.dialog = dialog;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDialog that = (UserDialog) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "UserDialog{" +
                "id=" + id +
                ", dialog=" + dialog +
                ", user=" + user +
                '}';
    }
}
