package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import socialnetwork.entities.Country;

import java.util.List;

/**
 * Created by Roman on 06.08.2017.
 */
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findCountryByIso(String iso);
    List<Country> findCountriesByContinent_IdOrderByName(Long id);
    @Query("Select DISTINCT c from Country c, User u WHERE c.id = u.country.id AND u.confirmed = true order by c.name")
    List<Country> findCountriesWithUsers();
    List<Country> findAllByOrderByName();
}
