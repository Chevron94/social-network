package socialnetwork;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import socialnetwork.entities.*;
import socialnetwork.repositories.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Roman on 09.08.2017.
 */
@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan
public class FirstStartHellofromApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FirstStartHellofromApplication.class, args);
        fillGenders(context);
        fillLanguages(context);
        fillLanguageLevels(context);
        fillGeography(context);
    }

    private static void fillGenders(ConfigurableApplicationContext context) {
        GenderRepository genderRepository = context.getBean(GenderRepository.class);
        if (genderRepository.count() == 0) {
            List<Gender> genders = new ArrayList<>();
            genders.add(new Gender("Male"));
            genders.add(new Gender("Female"));
            genderRepository.save(genders);
        }
    }

    private static void fillGeography(ConfigurableApplicationContext context) {
        ContinentRepository continentRepository = context.getBean(ContinentRepository.class);
        CountryRepository countryRepository = context.getBean(CountryRepository.class);
        CityRepository cityRepository = context.getBean(CityRepository.class);
        if (continentRepository.count() == 0) {
            Continent africa = new Continent("Africa");
            Continent asia = new Continent("Asia");
            Continent australia = new Continent("Australia and Oceania");
            Continent europe = new Continent("Europe");
            Continent northAmerica = new Continent("North America");
            Continent southAmerica = new Continent("South America");
            africa = continentRepository.save(africa);
            asia = continentRepository.save(asia);
            australia = continentRepository.save(australia);
            europe = continentRepository.save(europe);
            northAmerica = continentRepository.save(northAmerica);
            southAmerica = continentRepository.save(southAmerica);
            String[] locales = Locale.getISOCountries();

            HashMap<String, String> countriesAndContinents = new HashMap<>();
            HashMap<String, List<String>> citiesAndCountries = new HashMap<>();
            HashMap<String, String> isoFips = new HashMap<>();
            String countryToContinent = "cout.txt";
            String isoToFips = "GEODATASOURCE-COUNTRY.TXT";
            String countryToCity = "GEODATASOURCE-CITIES-FREE.TXT";

            Country country;
            String code;
            City city;
            BufferedReader br = null;
            try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(getFileFromResources(countryToContinent)));
                while ((sCurrentLine = br.readLine()) != null) {
                    String[] strings = sCurrentLine.split(",");
                    countriesAndContinents.put(strings[0].trim(), strings[strings.length - 1].trim());
                }

                br = new BufferedReader(new FileReader(getFileFromResources(isoToFips)));
                while ((sCurrentLine = br.readLine()) != null) {
                    String[] strings = sCurrentLine.split("\t");
                    if (!strings[0].trim().equals("-") && !strings[1].trim().equals("-"))
                        isoFips.put(strings[1].trim(), strings[0].trim());
                }

                br = new BufferedReader(new FileReader(getFileFromResources(countryToCity)));
                while ((sCurrentLine = br.readLine()) != null) {
                    String[] strings = sCurrentLine.split("\t");
                    if (strings[0].trim().length() > 0) {
                        List<String> cities = citiesAndCountries.get(strings[0].trim());
                        if (cities == null) {
                            cities = new ArrayList<>();
                        }
                        if (strings.length > 1 && strings[1].trim().length() > 0) {
                            cities.add(strings[1].trim());
                            citiesAndCountries.put(strings[0].trim(), cities);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            for (String countryCode : locales) {
                Locale obj = new Locale("", countryCode);
                code = countriesAndContinents.get(obj.getCountry()).trim();

                switch (code) {
                    case "EU":
                        country = new Country(obj.getDisplayCountry(Locale.ENGLISH), "/resources/images/flags/" + obj.getCountry().toLowerCase() + ".png", europe);
                        break;
                    case "AS":
                        country = new Country(obj.getDisplayCountry(Locale.ENGLISH), "/resources/images/flags/" + obj.getCountry().toLowerCase() + ".png", asia);
                        break;
                    case "AF":
                        country = new Country(obj.getDisplayCountry(Locale.ENGLISH), "/resources/images/flags/" + obj.getCountry().toLowerCase() + ".png", africa);
                        break;
                    case "OC":
                        country = new Country(obj.getDisplayCountry(Locale.ENGLISH), "/resources/images/flags/" + obj.getCountry().toLowerCase() + ".png", australia);
                        break;
                    case "NA":
                        country = new Country(obj.getDisplayCountry(Locale.ENGLISH), "/resources/images/flags/" + obj.getCountry().toLowerCase() + ".png", northAmerica);
                        break;
                    default:
                        country = new Country(obj.getDisplayCountry(Locale.ENGLISH), "/resources/images/flags/" + obj.getCountry().toLowerCase() + ".png", southAmerica);
                        break;
                }
                country.setIso(obj.getCountry());
                country = countryRepository.save(country);
                String fips = isoFips.get(country.getIso());
                if (citiesAndCountries.get(fips) == null) {
                    city = new City(country.getName(), country);
                    cityRepository.save(city);
                } else {
                    for (String s : citiesAndCountries.get(fips)) {
                        city = new City(s, country);
                        cityRepository.save(city);
                    }
                }


            }
        }
    }

    private static void fillLanguageLevels(ConfigurableApplicationContext context) {
        LanguageLevelRepository languageLevelRepository = context.getBean(LanguageLevelRepository.class);
        if (languageLevelRepository.count() == 0) {
            List<LanguageLevel> languageLevels = new ArrayList<>();
            languageLevels.add(new LanguageLevel("Beginner"));
            languageLevels.add(new LanguageLevel("Elementary"));
            languageLevels.add(new LanguageLevel("Pre-Intermediate"));
            languageLevels.add(new LanguageLevel("Intermediate"));
            languageLevels.add(new LanguageLevel("Upper-Intermediate"));
            languageLevels.add(new LanguageLevel("Advanced"));
            languageLevels.add(new LanguageLevel("Proficiency"));
            languageLevels.add(new LanguageLevel("Native Speaker"));
            languageLevelRepository.save(languageLevels);
        }
    }

    private static void fillLanguages(ConfigurableApplicationContext context) {
        LanguageRepository languageRepository = context.getBean(LanguageRepository.class);
        if (languageRepository.count() == 0) {
            String languageFile = "languages.txt";
            BufferedReader br = null;
            try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(getFileFromResources(languageFile)));
                List<Language> languages = new ArrayList<>();
                while ((sCurrentLine = br.readLine()) != null) {
                    String[] langs = sCurrentLine.split("\t");
                    languages.add(new Language(langs[1], langs[0], "/resources/images/flags/languages/" + langs[0].toLowerCase() + ".png"));
                }
                languageRepository.save(languages);
            } catch (Exception e) {
                //parsing error
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static String getFileFromResources(String name) {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        URL resource = classloader.getResource(name);
        return resource == null ? "" : resource.getFile();
    }

}
