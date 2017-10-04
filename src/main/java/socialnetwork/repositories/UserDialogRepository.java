package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import socialnetwork.entities.UserDialog;

/**
 * Created by Roman on 05.08.2017.
 */
public interface UserDialogRepository extends JpaRepository<UserDialog, Long>{
    @Query("SELECT ud FROM UserDialog ud WHERE ud.dialog.id = :dialogId AND ud.user.id <> :userId")
    UserDialog findUserDialogByDialog_IdAndAnotherUser_Id(@Param("dialogId") Long dialogId, @Param("userId") Long userId);
}
