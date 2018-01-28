package socialnetwork.beans;

import socialnetwork.entities.*;

import java.util.List;

/**
 * Created by Roman on 20.08.2017.
 */
public interface ListBean {
    Continent getContinent(Long id);
    List<Continent> getContinents();
    Country getCountry(Long id);
    List<Country> getCountries();
    List<Country> getCountries(Long continentId);
    List<Country> getCountriesWithUsers();
    City getCity(Long id);
    List<City> getCities(Long countryId, String name);
    Language getLanguage(Long id);
    List<Language> getLanguages();
    LanguageLevel getLanguageLevel(Long id);
    List<LanguageLevel> getLanguageLevels();
    Gender getGender(Long id);
    List<Gender> getGenders();
}
