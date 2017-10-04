package socialnetwork.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.File;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findFilesByMessage_Id(Long messageId);
}
