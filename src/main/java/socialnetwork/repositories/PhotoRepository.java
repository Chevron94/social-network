package socialnetwork.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import socialnetwork.entities.Photo;

import java.util.List;

/**
 * Created by Roman on 05.08.2017.
 */
public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findPhotosByAlbum_IdOrderByUploadedDesc(Long albumId, Pageable pageable);
    Long countPhotosByAlbum_Id(Long albumId);
}
