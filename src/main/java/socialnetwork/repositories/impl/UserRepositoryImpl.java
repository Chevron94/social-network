package socialnetwork.repositories.impl;

import org.springframework.stereotype.Repository;
import socialnetwork.entities.User;
import socialnetwork.repositories.UserRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

/**
 * Created by Roman on 05.08.2017.
 */
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findUsersByCustomFilter(Long idUser, Map<String, Object> params, Integer start, Integer limit) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("idUser", idUser);
        String jpa = "SELECT DISTINCT u.* FROM NETWORK_USER u " +
                "INNER JOIN LANGUAGE_USER lu ON lu.user_id = u.id " +
                "INNER JOIN COUNTRY c ON c.id = u.country_id ";
        String list = (String) params.get("list");
        if (list != null) {
            switch (list) {
                case "friends":
                    jpa += "INNER JOIN FRIEND_REQUEST f ON ((f.sender_id = :idUser AND f.receiver_id = u.id) OR (f.sender_id = u.id AND f.receiver_id = :idUser)) AND f.confirmed = true WHERE true ";
                    break;
                case "received":
                    jpa += "INNER JOIN FRIEND_REQUEST f ON (f.receiver_id = :idUser AND u.id = f.sender_id) AND f.confirmed = false WHERE true ";
                    break;
                case "sent":
                    jpa += "INNER JOIN FRIEND_REQUEST f ON (f.sender_id = :idUser AND u.id = f.receiver_id) AND f.confirmed = false WHERE true ";
                    break;
                default:
                    jpa += "LEFT JOIN FRIEND_REQUEST f ON ((f.sender_id = :idUser AND f.receiver_id = u.id) OR (f.receiver_id = :idUser AND f.sender_id = u.id)) WHERE f is NULL ";
                    break;
            }
        }


        Boolean isMale = (Boolean) params.get("male");
        Boolean isFemale = (Boolean) params.get("female");

        if (isFemale != isMale) {
            if (list == null) {
                jpa += isMale ? "WHERE u_gender.id = 1 " : "WHERE u.gender_id = 2 ";
            } else jpa += isMale ? "AND u_gender.id = 1 " : "AND u.gender_id = 2 ";
        }

        Integer ageFrom = (Integer) params.get("ageFrom");
        Integer ageTo = (Integer) params.get("ageTo");
        if (ageFrom != null && ageTo != null) {
            if (ageFrom > ageTo) {
                int tmp = ageTo;
                ageTo = ageFrom;
                ageFrom = tmp;
            }
            Date date = new Date();
            jpa += "AND EXTRACT(year FROM age(:date, u.date_of_birthday)) >= :ageFrom AND EXTRACT(year FROM age(:date, u.date_of_birthday)) <= :ageTo ";
            parameters.put("date", date);
            parameters.put("ageFrom", ageFrom);
            parameters.put("ageTo", ageTo);
        }
        if (params.get("login") != null) {
            String login = ((String) params.get("login")).trim();
            if (login.length() > 0) {
                jpa += "AND u.login LIKE :login ";
                parameters.put("login", login + "%");
            }
        }
        if (params.get("idLanguage") != null) {
            List<Long> idLanguages = Arrays.asList((Long[]) params.get("idLanguage"));
            if (!idLanguages.isEmpty() && idLanguages.get(0) != 0) {
                jpa += "AND lu.user_id = u.id AND lu.language_id in :idLanguages ";
                parameters.put("idLanguages", idLanguages);
            }
        }
        Long idCity = (Long) params.get("idCity");
        if (idCity != null && idCity > 0) {
            jpa += "AND u.city_id = :idCity ";
            parameters.put("idCity", idCity);
        } else {
            Long idCountry = (Long) params.get("idCountry");
            if (idCountry != null && idCountry > 0) {
                jpa += "AND u.country_id = :idCountry ";
                parameters.put("idCountry", idCountry);
            } else {
                Long idContinent = (Long) params.get("idContinent");
                if (idContinent != null && idContinent > 0) {
                    jpa += "AND c.continent_id = :idContinent ";
                    parameters.put("idContinent", idContinent);
                }
            }
        }
        jpa += "AND u.confirmed = true";
        Query query = entityManager.createNativeQuery(jpa, User.class);
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
        return query.setFirstResult(start * limit).setMaxResults(limit).getResultList();
    }
}
