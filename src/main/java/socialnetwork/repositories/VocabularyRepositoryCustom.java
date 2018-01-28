package socialnetwork.repositories;

import socialnetwork.dto.VocabularySearchDto;
import socialnetwork.entities.Vocabulary;

import java.util.List;

/**
 * Created by Roman on 21.01.2018 12:01.
 */
public interface VocabularyRepositoryCustom {
    List<Vocabulary> findVocabulariesByCustomFilter(VocabularySearchDto vocabularySearchDto);
}
