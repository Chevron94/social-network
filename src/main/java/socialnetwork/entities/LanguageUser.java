package socialnetwork.entities;

import javax.persistence.*;

/**
 * Created by roman on 15.09.15.
 */
@Entity
@Table(name = "LANGUAGE_USER")
public class LanguageUser {

    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LANGUAGE_LEVEL_ID", nullable = false)
    private LanguageLevel languageLevel;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "LANGUAGE_ID", nullable = false)
    private Language language;

    public LanguageUser() {
    }

    public LanguageUser(User user, Language language, LanguageLevel languageLevel) {
        this.languageLevel = languageLevel;
        this.user = user;
        this.language = language;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LanguageLevel getLanguageLevel() {
        return languageLevel;
    }

    public void setLanguageLevel(LanguageLevel languageLevel) {
        this.languageLevel = languageLevel;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LanguageUser that = (LanguageUser) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "LanguageUser{" +
                "id=" + id +
                ", languageLevel=" + languageLevel +
                ", user=" + user +
                ", language=" + language +
                '}';
    }
}
