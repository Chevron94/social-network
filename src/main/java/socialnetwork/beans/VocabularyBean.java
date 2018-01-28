package socialnetwork.beans;

import socialnetwork.dto.VocabularySearchDto;
import socialnetwork.dto.creation.VocabularyCreationDto;
import socialnetwork.dto.creation.VocabularyRecordCreationDto;
import socialnetwork.entities.Vocabulary;
import socialnetwork.entities.VocabularyRecord;

import java.util.List;

/**
 * Created by Roman on 21.01.2018 11:40.
 */
public interface VocabularyBean {
    Vocabulary createVocabulary(VocabularyCreationDto vocabularyCreationDto);
    List<Vocabulary> getVocabularies(Long userId);
    List<Vocabulary> getVocabularies(VocabularySearchDto vocabularySearchDto);
    Vocabulary getVocabulary(Long vocabularyId, Long userId);
    void deleteVocabulary(Long userId, Long vocabularyId);
    VocabularyRecord createVocabularyRecord(VocabularyRecordCreationDto creationDto);
    VocabularyRecord updateVocabularyRecord(Long vocabularyRecordId, VocabularyRecordCreationDto creationDto);
    List<VocabularyRecord> getVocabularyRecords(Long userId, Long vocabulary, String word, String translation);
    void deleteVocabularyRecord(Long userId, Long vocabularyRecordId);
}
