package socialnetwork.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import socialnetwork.entities.Dialog;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface DialogRepository extends JpaRepository<Dialog, Long> {
    @Query(value = "SELECT u1.dialog " +
            "FROM UserDialog u1, UserDialog u2 " +
            "WHERE u1.dialog = u2.dialog AND " +
            "u1.user.id = :idUser1 AND u2.user.id = :idUser2 AND " +
            "2 = (SELECT COUNT(u3) FROM UserDialog u3 WHERE u3.dialog.id = u1.dialog.id)")
    Dialog findDialogByTwoUsers(@Param("idUser1") Long idUser1,@Param("idUser2") Long idUser2);
    @Query(value = "SELECT DISTINCT ud.dialog FROM UserDialog ud " +
            "WHERE ud.user.id = :id " +
            "ORDER BY ud.dialog.lastMessageDate DESC")
    List<Dialog> findDialogsByUser_Id(@Param("id") Long id, Pageable pageable);
    @Query(value = "SELECT DISTINCT ud.dialog FROM UserDialog ud " +
            "WHERE ud.user.id = :id " +
            "AND EXISTS (SELECT m FROM Message m WHERE m.read = false AND m.user.id <> :id AND m.dialog.id = ud.dialog.id)")
    List<Dialog> findDialogsByUser_IdWithUnreadMessages(@Param("id") Long id);
}
