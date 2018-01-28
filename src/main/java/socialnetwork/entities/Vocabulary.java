package socialnetwork.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 21.01.2018 11:28.
 */
@Entity
@Table(name = "VOCABULARY")
public class Vocabulary {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "FROM_ID", nullable = false)
    private Language from;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TO_ID", nullable = false)
    private Language to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "vocabulary")
    @JsonIgnore
    private transient List<VocabularyRecord> records = new ArrayList<>();

    public Vocabulary() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Language getFrom() {
        return from;
    }

    public void setFrom(Language from) {
        this.from = from;
    }

    public Language getTo() {
        return to;
    }

    public void setTo(Language to) {
        this.to = to;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Vocabulary(Language from, Language to, User user) {
        this.from = from;
        this.to = to;
        this.user = user;


    }

    public List<VocabularyRecord> getRecords() {
        return records;
    }

    public void setRecords(List<VocabularyRecord> records) {
        this.records = records;
    }

    @Override
    public String toString() {
        return "Vocabulary{" +
                "id=" + id +
                ", from=" + from +
                ", to=" + to +
                ", user=" + user +
                '}';
    }
}
