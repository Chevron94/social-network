package socialnetwork.beans;

import org.springframework.web.multipart.MultipartFile;
import socialnetwork.entities.Album;
import socialnetwork.entities.Photo;
import socialnetwork.exceptions.AccessDeniedException;

import java.util.List;

/**
 * Created by Roman on 09.08.2017.
 */
public interface AlbumBean {
    Album createAlbum(Long userId, String albumName);
    Album renameAlbum(Long userId, Long albumId, String newName);
    Album getAlbum(Long albumId);
    Album getAlbum(Long userId, String albumName);
    List<Album> getAlbums(Long userId, Integer page, Integer count);
    void deleteAlbum(Long userId, Long albumId);
    Album storePhotos(Long userId, Long albumId, List<MultipartFile> photos);
    Photo getPhoto(Long photoId);
    List<Photo> getPhotos(Long albumId, Integer page, Integer count);
    Long countPhotosInAlbum(Long albumId);
    Long countAlbums(Long userId);
    void copyPhotoToAlbum(Long userId, Long photoId, Long destinationAlbumId);
    void deletePhoto(Long userId, Long photoId);
    //отметить
}
