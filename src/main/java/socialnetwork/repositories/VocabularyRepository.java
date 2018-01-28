package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.Vocabulary;

import java.util.List;

/**
 * Created by Roman on 21.01.2018 11:54.
 */
public interface VocabularyRepository extends JpaRepository<Vocabulary, Long>, VocabularyRepositoryCustom {
    List<Vocabulary> findVocabulariesByUser_Id(Long userId);
    Vocabulary findVocabularyByIdAndUserId(Long vocabularyId, Long userId);
}
