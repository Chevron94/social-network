package socialnetwork.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import socialnetwork.entities.Message;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findMessagesByDialog_IdOrderByDateTimeDesc(Long dialogId, Pageable pageable);
    @Query(value = "SELECT m FROM Message m WHERE m.read = false AND m.user.id <> :userId AND m.dialog.id = :dialogId")
    List<Message> findMessagesToRead(@Param("userId") Long userId, @Param("dialogId") Long dialogId);
    Long countMessagesByUser_IdAndDialog_IdAndReadIsFalse(Long userId, Long dialogId);
}
