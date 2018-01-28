package socialnetwork.dto;

import socialnetwork.entities.Vocabulary;
import socialnetwork.entities.VocabularyRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 28.01.2018 16:23.
 */
public class VocabularyDto {
    private Long id;
    private String from;
    private String to;
    private Long userId;
    private List<VocabularyRecordDto> vocabularyRecords;

    public VocabularyDto() {
        vocabularyRecords = new ArrayList<>();
    }


    public VocabularyDto(Vocabulary vocabulary) {
        this.id = vocabulary.getId();
        this.from = vocabulary.getFrom().getName();
        this.to = vocabulary.getTo().getName();
        this.userId = vocabulary.getUser().getId();
        this.vocabularyRecords = convert(vocabulary.getRecords());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private List<VocabularyRecordDto> convert(List<VocabularyRecord> vocabularyRecords){
        List<VocabularyRecordDto> vocabularyRecordDtos = new ArrayList<>(vocabularyRecords.size());
        for (VocabularyRecord vocabularyRecord: vocabularyRecords){
            vocabularyRecordDtos.add(new VocabularyRecordDto(vocabularyRecord));
        }
        return vocabularyRecordDtos;
    }

    @Override
    public String toString() {
        return "VocabularyDto{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", userId=" + userId +
                '}';
    }
}
