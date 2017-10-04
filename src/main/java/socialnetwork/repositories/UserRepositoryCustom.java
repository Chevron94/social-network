package socialnetwork.repositories;

import socialnetwork.entities.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Roman on 05.08.2017.
 */
public interface UserRepositoryCustom {
    List<User> findUsersByCustomFilter(Long idUser, Map<String,Object> params, Integer start, Integer limit);
}
