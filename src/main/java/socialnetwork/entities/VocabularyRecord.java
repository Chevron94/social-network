package socialnetwork.entities;

import javax.persistence.*;

/**
 * Created by Roman on 21.01.2018 11:32.
 */
@Entity
@Table(name = "VOCABULARY_RECORD")
public class VocabularyRecord {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "WORD")
    private String word;

    @Column(name = "TRANSLATION")
    private String translation;

    @Column(name = "COMMENT", columnDefinition = "TEXT")
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vocabulary_ID", nullable = false)
    private Vocabulary vocabulary;

    public VocabularyRecord() {
    }

    public VocabularyRecord(String word, String translation, String comment, Vocabulary vocabulary) {
        this.word = word;
        this.translation = translation;
        this.comment = comment;
        this.vocabulary = vocabulary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Vocabulary getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(Vocabulary vocabulary) {
        this.vocabulary = vocabulary;
    }

    @Override
    public String toString() {
        return "VocabularyRecord{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", comment='" + comment + '\'' +
                ", vocabulary=" + vocabulary +
                '}';
    }
}
