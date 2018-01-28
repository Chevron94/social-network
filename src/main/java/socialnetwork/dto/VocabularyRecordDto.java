package socialnetwork.dto;

import socialnetwork.entities.Vocabulary;
import socialnetwork.entities.VocabularyRecord;

/**
 * Created by Roman on 28.01.2018 16:29.
 */
public class VocabularyRecordDto {
    private Long id;
    private String word;
    private String translation;
    private String comment;
    private String vocabulary;

    public VocabularyRecordDto() {
    }

    public VocabularyRecordDto(VocabularyRecord vocabularyRecord) {
        this.id = vocabularyRecord.getId();
        this.word = vocabularyRecord.getWord();
        this.translation = vocabularyRecord.getTranslation();
        this.comment = vocabularyRecord.getComment();
        Vocabulary vocabulary = vocabularyRecord.getVocabulary();
        this.vocabulary = vocabulary.getFrom().getName() + "-" + vocabulary.getTo().getName();
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

    public String getVocabulary() {
        return vocabulary;
    }

    public void setVocabulary(String vocabularyId) {
        this.vocabulary = vocabularyId;
    }

    @Override
    public String toString() {
        return "VocabularyRecordDto{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", comment='" + comment + '\'' +
                ", vocabularyId=" + vocabulary +
                '}';
    }
}
