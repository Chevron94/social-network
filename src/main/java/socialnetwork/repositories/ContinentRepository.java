package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.Continent;

/**
 * Created by Roman on 05.08.2017.
 */
public interface ContinentRepository extends JpaRepository<Continent, Long> {
}
