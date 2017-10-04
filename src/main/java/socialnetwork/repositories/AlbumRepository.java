package socialnetwork.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.Album;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Album findAlbumByUser_IdAndName(Long id, String name);
    List<Album> findAlbumsByUser_IdOrderByCreationDateDesc(Long id, Pageable pageable);
    Long countAlbumsByUser_Id(Long id);
}
