package socialnetwork.repositories.impl;

import org.springframework.stereotype.Repository;
import socialnetwork.entities.User;
import socialnetwork.repositories.UserRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

/**
 * Created by Roman on 05.08.2017.
 */
@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;
    @Override
    public List<User> findUsersByCustomFilter(Long idUser, Map<String, Object> params, Integer start, Integer limit) {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("idUser",idUser);
        String jpa = "SELECT DISTINCT u FROM User u, LanguageUser lu";
        String list = (String) params.get("list");
        if(list!= null) {
            jpa+= ", FriendRequest f ";
            switch (list) {
                case "friends":
                    jpa += "WHERE (f.sender.id = :idUser OR f.receiver.id = :idUser) AND (f.sender.id = u.id OR f.receiver.id = u.id) AND u.id <> :idUser AND f.confirmed = true ";
                    break;
                case "received":
                    jpa += "WHERE (f.receiver.id = :idUser AND u.id = f.sender.id) AND f.confirmed = false ";
                    break;
                case "sent":
                    jpa += "WHERE (f.sender.id = :idUser AND u.id = f.receiver.id) AND f.confirmed = false ";
                    break;
                default:
                    jpa += "WHERE u.id NOT IN (SELECT DISTINCT u1.id FROM User u1, FriendRequest f1 WHERE (f1.sender.id = :idUser OR f1.receiver.id = :idUser) AND (f1.sender.id = u1.id OR f1.receiver.id = u1.id)) AND u.id <> :idUser ";
                    break;
            }
        }



        Boolean isMale = (Boolean)params.get("male");
        Boolean isFemale = (Boolean)params.get("female");

        if (isFemale != isMale)
        {
            if(list==null) {
                jpa += isMale ? "WHERE u.gender.id = 1 " : "WHERE u.gender.id = 2 ";
            }else jpa += isMale ? "AND u.gender.id = 1 " : "AND u.gender.id = 2 ";
        }

        Integer ageFrom = (Integer)params.get("ageFrom");
        Integer ageTo = (Integer)params.get("ageTo");
        if (ageFrom != null && ageTo != null){
            if (ageFrom > ageTo)
            {
                int tmp = ageTo;
                ageTo = ageFrom;
                ageFrom = tmp;
            }
            Date date = new Date();
            jpa += "AND EXTRACT(year FROM age(:date, u.birthday)) >= :ageFrom AND EXTRACT(year FROM age(:date, u.birthday)) <= :ageTo ";
            parameters.put("date",date);
            parameters.put("ageFrom", ageFrom);
            parameters.put("ageTo",ageTo);
        }
        if (params.get("login") != null){
            String login = ((String)params.get("login")).trim();
            if (login.length()>0){
                jpa+= "AND u.login LIKE :login ";
                parameters.put("login", login+"%");
            }
        }
        if(params.get("idLanguage") != null){
            List<Long> idLanguages = Arrays.asList((Long[])params.get("idLanguage"));
            if (idLanguages.get(0) != 0){
                jpa+= "AND lu.user.id = u.id AND lu.language.id in :idLanguages ";
                parameters.put("idLanguages",idLanguages);
            }
        }
        Long idCity = (Long)params.get("idCity");
        if (idCity != null && idCity > 0) {
            jpa += "AND u.city.id = :idCity ";
            parameters.put("idCity", idCity);
        }else{
            Long idCountry =(Long)params.get("idCountry");
            if (idCountry != null && idCountry > 0)
            {
                jpa += "AND u.country.id = :idCountry ";
                parameters.put("idCountry", idCountry);
            }else
            {
                Long idContinent = (Long)params.get("idContinent");
                if (idContinent != null && idContinent > 0) {
                    jpa += "AND u.country.continent.id = :idContinent ";
                    parameters.put("idContinent", idContinent);
                }
            }
        }
        jpa+="AND u.confirmed = true";
        Query query = entityManager.createQuery(jpa);
        for(Map.Entry<String,Object> entry : parameters.entrySet()){
            query.setParameter(entry.getKey(),entry.getValue());
        }
        return query.setFirstResult(start*limit).setMaxResults(limit).getResultList();
    }
}
