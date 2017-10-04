package socialnetwork.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.LanguageUser;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface LanguageUserRepository extends JpaRepository<LanguageUser, Long> {
    LanguageUser findLanguageUserByUser_IdAndLanguage_Id(Long userId, Long languageId);
    List<LanguageUser> findLanguageUsersByUser_IdOrderByLanguageLevelDesc(Long userId);
    List<LanguageUser> findLanguageUsersByLanguage_Id(Long languageId, Pageable pageable);
}
