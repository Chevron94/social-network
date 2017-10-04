package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.Language;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface LanguageRepository extends JpaRepository<Language, Long> {
    List<Language> findAllByOrderByName();
}
