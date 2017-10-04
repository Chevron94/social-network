package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.City;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findCitiesByCountry_IdOrderByName(Long id);
    List<City> findCitiesByCountry_IdAndNameStartsWithOrderByName(Long id, String name);
}
