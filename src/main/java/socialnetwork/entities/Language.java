package socialnetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 14.09.15.
 */
@Entity
@Table(name = "LANGUAGE")
public class Language {
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="NAME")
    private String name;

    @Column(name = "CODE")
    private String code;

    @Column(name = "PICTURE")
    private String picture;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "language", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<LanguageUser> languageUsers = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "from", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vocabulary> fromVocabularies = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "to", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vocabulary> toVocabularies = new ArrayList<>();


    public Language() {
    }

    public Language(String name, String code, String picture) {
        this.name = name;
        this.code = code;
        this.picture = picture;
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

    public String getCode() {
        return code;
    }

    public void setCode(String pictureURL) {
        this.code = pictureURL;
    }

    public List<LanguageUser> getLanguageUsers() {
        return languageUsers;
    }

    public void setLanguageUsers(List<LanguageUser> languageUsers) {
        this.languageUsers = languageUsers;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public List<Vocabulary> getFromVocabularies() {
        return fromVocabularies;
    }

    public void setFromVocabularies(List<Vocabulary> fromVocabularies) {
        this.fromVocabularies = fromVocabularies;
    }

    public List<Vocabulary> getToVocabularies() {
        return toVocabularies;
    }

    public void setToVocabularies(List<Vocabulary> toVocabularies) {
        this.toVocabularies = toVocabularies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Language language = (Language) o;

        return !(id != null ? !id.equals(language.id) : language.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
