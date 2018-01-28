package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.VocabularyRecord;

import java.util.List;

/**
 * Created by Roman on 21.01.2018 12:16.
 */
public interface VocabularyRecordRepository extends JpaRepository<VocabularyRecord, Long> {
    List<VocabularyRecord> findVocabularyRecordsByVocabulary_Id(Long vocabularyId);
    List<VocabularyRecord> findVocabularyRecordsByVocabulary_IdAndWordStartsWith(Long vocabularyId, String word);
    List<VocabularyRecord> findVocabularyRecordsByVocabulary_IdAndTranslationStartsWith(Long vocabularyId, String translation);
    List<VocabularyRecord> findVocabularyRecordsByVocabulary_IdAndWordStartsWithAndTranslationStartsWith(Long vocabularyId, String word, String translation);
}
