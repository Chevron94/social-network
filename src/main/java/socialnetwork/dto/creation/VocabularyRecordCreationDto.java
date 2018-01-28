package socialnetwork.dto.creation;

/**
 * Created by Roman on 28.01.2018 17:31.
 */
public class VocabularyRecordCreationDto {
    private Long userId;
    private Long vocabularyId;
    private String word;
    private String translation;
    private String comment;

    public VocabularyRecordCreationDto() {
    }

    public VocabularyRecordCreationDto(Long userId, Long vocabularyId, String word, String translation, String comment) {
        this.word = word;
        this.translation = translation;
        this.comment = comment;
        this.vocabularyId = vocabularyId;
        this.userId = userId;
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

    public Long getVocabularyId() {
        return vocabularyId;
    }

    public void setVocabularyId(Long vocabularyId) {
        this.vocabularyId = vocabularyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "VocabularyRecordCreationDto{" +
                "userId=" + userId +
                ", vocabularyId=" + vocabularyId +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
