package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.Gender;

/**
 * Created by Roman on 05.08.2017.
 */
public interface GenderRepository extends JpaRepository<Gender, Long> {
}
