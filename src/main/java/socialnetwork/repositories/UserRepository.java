package socialnetwork.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import socialnetwork.entities.User;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    User findUserByEmailAndConfirmedIsTrue(String email);
    User findUserByEmail(String email);
    User findUserByLoginAndPasswordAndConfirmedIsTrue(String login, String password);
    User findUserByLogin(String login);
    User findUserByToken(String token);
    User findUserByActivationToken(String token);
    User findUserByResetPasswordTokenAndConfirmedIsTrue(String token);
    User findUserByRememberMeTokenAndConfirmedIsTrue(String token);
    List<User> findUsersByCity_IdAndConfirmedIsTrue(Long cityID, Pageable pageable);
    List<User> findUsersByCountry_IdAndConfirmedIsTrue(Long countryId, Pageable pageable);
    List<User> findUsersByGender_IdAndConfirmedIsTrue(Long genderID, Pageable pageable);
    @Query("select u.user from UserDialog u WHERE u.dialog.id = :id AND u.user.confirmed = true")
    List<User> findUsersByDialog_Id(@Param("id") Long dialogId);
}
