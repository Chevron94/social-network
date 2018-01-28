package socialnetwork.beans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import socialnetwork.beans.ListBean;
import socialnetwork.entities.*;
import socialnetwork.repositories.*;

import java.util.List;

/**
 * Created by Roman on 20.08.2017.
 */
@Component
public class ListBeanImpl implements ListBean {

    @Autowired
    private ContinentRepository continentRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private LanguageLevelRepository languageLevelRepository;
    @Autowired
    private GenderRepository genderRepository;

    @Override
    public Continent getContinent(Long id) {
        return continentRepository.findOne(id);
    }

    @Override
    public List<Continent> getContinents() {
        return continentRepository.findAll();
    }

    @Override
    public Country getCountry(Long id) {
        return countryRepository.findOne(id);
    }

    @Override
    public List<Country> getCountries() {
        return countryRepository.findAllByOrderByName();
    }

    @Override
    public List<Country> getCountries(Long continentId) {
        return countryRepository.findCountriesByContinent_IdOrderByName(continentId);
    }

    @Override
    public List<Country> getCountriesWithUsers() {
        return countryRepository.findCountriesWithUsers();
    }

    @Override
    public City getCity(Long id) {
        return cityRepository.findOne(id);
    }

    @Override
    public List<City> getCities(Long countryId, String name) {
        return cityRepository.findCitiesByCountry_IdAndNameStartsWithOrderByName(countryId, name);
    }

    @Override
    public Language getLanguage(Long id) {
        return languageRepository.findOne(id);
    }

    @Override
    public List<Language> getLanguages() {
        return languageRepository.findAllByOrderByName();
    }

    @Override
    public LanguageLevel getLanguageLevel(Long id) {
        return languageLevelRepository.findOne(id);
    }

    @Override
    public List<LanguageLevel> getLanguageLevels() {
        return languageLevelRepository.findAll();
    }

    @Override
    public Gender getGender(Long id) {
        return genderRepository.getOne(id);
    }

    @Override
    public List<Gender> getGenders() {
        return genderRepository.findAll();
    }
}
