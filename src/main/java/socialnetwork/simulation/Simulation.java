package socialnetwork.simulation;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import socialnetwork.beans.AlbumBean;
import socialnetwork.beans.DialogBean;
import socialnetwork.beans.ListBean;
import socialnetwork.beans.UserBean;
import socialnetwork.entities.*;
import socialnetwork.repositories.FriendRequestRepository;
import socialnetwork.repositories.LanguageUserRepository;
import socialnetwork.repositories.UserRepository;

import java.util.*;

/**
 * Created by Roman on 01.04.2018 12:29.
 */
@Component
public class Simulation {

    private static final Logger LOGGER = LoggerFactory.getLogger(Simulation.class);

    private static final Long EUROPE_ID = 4L;
    private static final Long ASIA_ID = 2L;
    private static final Long AFRICA_ID = 1L;
    private static final Long NORTH_AMERICA_ID = 5L;
    private static final Long SOUTH_AMERICA_ID = 6L;
    private static final Long AUSTRALIA_ID = 3L;
    private static final Integer EUROPE_USERS = 56000;
    private static final Integer ASIA_USERS = 20000;
    private static final Integer AFRICA_USERS = 7000;
    private static final Integer NORTH_AMERICA_USERS = 13000;
    private static final Integer SOUTH_AMERICA_USERS = 3000;
    private static final Integer AUSTRALIA_USERS = 1000;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LanguageUserRepository languageUserRepository;
    @Autowired
    private AlbumBean albumBean;
    @Autowired
    private DialogBean dialogBean;
    @Autowired
    private ListBean listBean;
    @Autowired
    private UserBean userBean;

    private void generateUsers(Long continentID, Integer amount, Integer threadNumber) {
        Random random = new Random();
        Continent continent = listBean.getContinent(continentID);
        List<Gender> genders = listBean.getGenders();
        List<Country> countries = listBean.getCountries(continentID);
        List<Language> languages = listBean.getLanguages();
        List<LanguageLevel> languageLevels = listBean.getLanguageLevels();
        List<Album> albums = new ArrayList<>();
        List<User> users = Collections.synchronizedList(new ArrayList<>());
        List<LanguageUser> languageUsers = Collections.synchronizedList(new ArrayList<>());
        for (Integer i = 0; i < amount; i++) {
            User user = new User();
            user.setLogin(continent.getName() + "_" + (amount * threadNumber + i));
            user.setName(continent.getName() + "_" + (amount * threadNumber + i));
            user.setPassword(UUID.randomUUID().toString());
            user.setConfirmed(true);
            user.setBirthday(DateUtils.addDays(new Date(), -random.nextInt(18250)));
            user.setEmail(continent.getName() + "_" + (amount * threadNumber + i) + "@example.com");
            user.setGender(genders.get(random.nextInt(genders.size())));
            user.setOnline(true);
            user.setLocked(false);
            user.setRole(1);
            user.setCountry(countries.get(random.nextInt(countries.size())));
            List<City> cities = listBean.getCities(user.getCountry().getId(), getRandomLetter());
            Integer retry = 0;
            while (cities.isEmpty()) {
                if (retry < 5) {
                    cities = listBean.getCities(user.getCountry().getId(), getRandomLetter());
                    retry++;
                } else {
                    cities = listBean.getCities(user.getCountry().getId(), "");
                }
            }
            user.setCity(cities.get(random.nextInt(cities.size())));
            users.add(user);
        }
        users = userRepository.save(users);
        for (User user : users) {
            albumBean.createAlbum(user.getId(), "Main");
            Integer languagesAmount = random.nextInt(4) + 1;
            Set<Language> usedLanguages = new HashSet<>();
            for (Integer i = 0; i < languagesAmount; i++) {
                LanguageUser languageUser = new LanguageUser();
                languageUser.setUser(user);
                Language language;
                do {
                    language = languages.get(random.nextInt(languages.size()));
                } while (usedLanguages.contains(language));
                usedLanguages.add(language);
                languageUser.setLanguage(language);
                languageUser.setLanguageLevel(languageLevels.get(random.nextInt(languageLevels.size())));
                languageUsers.add(languageUser);
            }
        }
        languageUserRepository.save(languageUsers);
    }

    private String getRandomLetter() {
        Random r = new Random();
        String s = "";
        s += (char) (r.nextInt(26) + 'a');
        return s.toUpperCase();
    }

    private void generateUsersAsync(Long continentID, Integer amount) {
        Thread[] threads = new Thread[5];
        threads[0] = new Thread(() -> generateUsers(continentID, amount / 5, 0), "0");
        threads[0].start();
        threads[1] = new Thread(() -> generateUsers(continentID, amount / 5, 1), "1");
        threads[1].start();
        threads[2] = new Thread(() -> generateUsers(continentID, amount / 5, 2), "2");
        threads[2].start();
        threads[3] = new Thread(() -> generateUsers(continentID, amount / 5, 3), "3");
        threads[3].start();
        threads[4] = new Thread(() -> generateUsers(continentID, amount / 5, 4), "4");
        threads[4].start();
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException ex) {
            LOGGER.info("Something went wrong");
        }
    }

    public void generateFriends(Integer amount){
        Random random = new Random();
        for (Integer i = 0; i<amount; i++){
            Long sender = random.nextLong()%100001 + 1;
            Long receiver = random.nextLong()%100001 + 1;
            userBean.sendFriendRequest(sender, receiver);
            userBean.updateFriendRequest(sender, receiver);
        }
    }

    private void generateFriendsAsync(Integer amount) {
        Thread[] threads = new Thread[5];
        for (Integer i = 0; i < 5; i++){
            threads[i] = new Thread(() -> generateFriends(amount / 5));
            threads[i].start();
        }
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException ex) {
            LOGGER.info("Something went wrong");
        }
    }

    public void simulate(Boolean generateUsers, Boolean generateFriends, Boolean generateDialogs, Boolean generateMessages) {
        if (generateUsers) {
            Integer generated = 0;
            generateUsersAsync(EUROPE_ID, EUROPE_USERS);
            generated += EUROPE_USERS;
            LOGGER.info("generated {} users", generated);
            generateUsersAsync(ASIA_ID, ASIA_USERS);
            generated += ASIA_USERS;
            LOGGER.info("generated {} users", generated);
            generateUsersAsync(AFRICA_ID, AFRICA_USERS);
            generated += AFRICA_USERS;
            LOGGER.info("generated {} users", generated);
            generateUsersAsync(AUSTRALIA_ID, AUSTRALIA_USERS);
            generated += AUSTRALIA_USERS;
            LOGGER.info("generated {} users", generated);
            generateUsersAsync(NORTH_AMERICA_ID, NORTH_AMERICA_USERS);
            generated += NORTH_AMERICA_USERS;
            LOGGER.info("generated {} users", generated);
            generateUsersAsync(SOUTH_AMERICA_ID, SOUTH_AMERICA_USERS);
            generated += SOUTH_AMERICA_USERS;
            LOGGER.info("generated {} users", generated);
            LOGGER.info("User generation complete");
        }
        if (generateFriends){
            generateFriendsAsync(1000000);
            LOGGER.info("Generated 1000000 friend requests");
        }
    }

}
