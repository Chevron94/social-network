package socialnetwork.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import socialnetwork.beans.AlbumBean;
import socialnetwork.entities.Album;
import socialnetwork.entities.Photo;
import socialnetwork.entities.User;
import socialnetwork.exceptions.AccessDeniedException;
import socialnetwork.exceptions.NameExistsException;
import socialnetwork.helpers.Converters;
import socialnetwork.helpers.UrlConstructor;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
 * Created by Roman on 06.03.2018 12:24.
 */
@RestController
@RequestMapping(value = "/api/v1/albums")
public class AlbumsAPIController extends GenericAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumsAPIController.class);

    @Autowired
    private AlbumBean albumBean;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity loadAlbums(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "count", defaultValue = "100") Integer count,
            HttpServletRequest request) {
        if (userId == null) {
            User user = getUser(request);
            userId = user.getId();
        }
        List<Album> albums = albumBean.getAlbums(userId, page, count);
        for (Album album : albums) {
            constructUrl(request.getRequestURL().toString(), album.getPhotos());
        }
        return ResponseEntity.ok(Converters.convertAlbumsToAlbumDtos(albums));
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity createAlbum(@RequestBody String name, HttpServletRequest request) {
        User user = getUser(request);
        try {
            Album album = albumBean.createAlbum(user.getId(), name);
            return ResponseEntity.status(HttpStatus.CREATED).body(Converters.convertAlbumsToAlbumDtos(Collections.singletonList(album)));
        } catch (NameExistsException ex) {
            LOGGER.warn("User with id {} already has album with name {}", user.getId(), name);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with id " + user.getId() + " already has album with name " + name);
        }
    }

    //Delete album
    @RequestMapping(value = "/{albumId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAlbum(@PathVariable("albumId") Long id, HttpServletRequest request) {
        User user = getUser(request);
        try {
            albumBean.deleteAlbum(user.getId(), id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/{albumId}", method = RequestMethod.PUT)
    public ResponseEntity renameAlbum(@PathVariable("albumId") Long id, @RequestParam("newName") String newName, HttpServletRequest request) {
        User user = getUser(request);
        try {
            albumBean.renameAlbum(user.getId(), id, newName);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        } catch (NameExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    //Load more photos
    @RequestMapping(value = "/{albumId}/photos", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity loadMorePhotos(@PathVariable(value = "albumId") Long albumId,
                                  @RequestParam(value = "page") Integer page,
                                  @RequestParam(value = "count") Integer count,
                                  HttpServletRequest request) {
        List<Photo> photos = albumBean.getPhotos(albumId, page, count);
        constructUrl(request.getRequestURL().toString(), photos);
        return ResponseEntity.ok(Converters.convertPhotosToPhotoDtos(photos));
    }

    //Get number photos in album
    @RequestMapping(value = "/{albumId}/photosCnt", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getCount(@PathVariable(value = "albumId") Long idAlbum) {
        return ResponseEntity.ok(albumBean.countPhotosInAlbum(idAlbum));
    }

    //Delete album
    @RequestMapping(value = "/{albumId}/photos/{photoId}", method = RequestMethod.DELETE)
    public ResponseEntity deletePhoto(@PathVariable("photoId") Long albumId, @PathVariable("photoId") Long id, HttpServletRequest request) {
        User user = getUser(request);
        try {
            albumBean.deletePhoto(user.getId(), id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException ex) {
            LOGGER.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    private void constructUrl(String requestUrl, List<Photo> photos) {
        for (Photo photo : photos) {
            photo.setPhotoUrl(UrlConstructor.constructUrl(requestUrl, photo.getPhotoUrl()));
        }
    }

}
