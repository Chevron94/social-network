package socialnetwork.controllers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import socialnetwork.beans.AlbumBean;
import socialnetwork.beans.UserBean;
import socialnetwork.dto.AlbumDto;
import socialnetwork.dto.PhotoDto;
import socialnetwork.entities.Album;
import socialnetwork.entities.Photo;
import socialnetwork.exceptions.AccessDeniedException;
import socialnetwork.exceptions.NameExistsException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Роман on 31.03.2016.
 */
@Controller
public class AlbumController extends GenericController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumController.class);
    @Autowired
    private UserBean userBean;

    @Autowired
    private AlbumBean albumBean;

    @RequestMapping(value = "/albums", method = RequestMethod.GET)
    public String getAllAlbums(HttpServletRequest request, Model model) {
        Long idUser = getUserId(request);
        model.addAttribute("requestUser", userBean.getUser(idUser));
        return "albums";
    }

    @RequestMapping(value = "/user{id}/albums", method = RequestMethod.GET)
    public String getAllAlbums(@PathVariable String id, Model model, HttpServletRequest request) {
        getUserId(request);
        Long idRequestUser = Long.valueOf(id);
        List<Album> albums = albumBean.getAlbums(idRequestUser, 0, 5);
        if (albums.isEmpty()) {
            return "404";
        }
        model.addAttribute("requestUser", userBean.getUser(idRequestUser));
        return "albums";
    }

    //Getting photos by Album
    @RequestMapping(value = "/albums/{id}", method = RequestMethod.GET)
    public String getAlbum(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        getUserId(request);
        Album album = albumBean.getAlbum(id);
        if (album == null)
            return "404";
        constructUrl(request.getRequestURL().toString(), album.getPhotos());
        model.addAttribute("album", album);
        return "album";
    }

    //AddingPhotos
    @RequestMapping(value = "/albums/{id}", method = RequestMethod.POST)
    public String uploadImages(@PathVariable("id") Long id, HttpServletRequest request, MultipartRequest multipartRequest, Model model) {
        Long userId = getUserId(request);
        List<MultipartFile> files = multipartRequest.getFiles("files");
        Album album = albumBean.storePhotos(userId, id, files);
        model.addAttribute("album", album);
        return "album";
    }

    //JS METHODS
    //Load more albums
    @RequestMapping(value = "/albums/more", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity loadMoreAlbums(
            @RequestParam(value = "idUser") Long idRequestUser,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "count") Integer count,
            HttpServletRequest request) {
        List<Album> albums = albumBean.getAlbums(idRequestUser, page, count);
        for (Album album : albums) {
            constructUrl(request.getRequestURL().toString(), album.getPhotos());
        }
        return ResponseEntity.ok(convertToAlbumDto(albums));

    }

    // Create new album
    @RequestMapping(value = "/albums", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity newAlbum(@RequestParam(value = "name") String name, HttpServletRequest request) {
        Long userId = getUserId(request);
        Album album = albumBean.createAlbum(userId, name);
        return ResponseEntity.ok(album.getId());
    }

    //Delete album
    @RequestMapping(value = "/albums/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deleteAlbum(@PathVariable("id") Long id, HttpServletRequest request) {
        Long userId =  getUserId(request);
        try {
            albumBean.deleteAlbum(userId, id);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "/albums/{id}/rename", method = RequestMethod.POST)
    public ResponseEntity renameAlbum(@PathVariable("id") Long id, @RequestParam("newName") String newName, HttpServletRequest request) {
        Long userId = getUserId(request);
        try {
            albumBean.renameAlbum(userId, id, newName);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        } catch (NameExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }

    //Load more photos
    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity loadMorePhotos(@RequestParam(value = "idAlbum") Long idAlbum,
                                  @RequestParam(value = "page") Integer page,
                                  @RequestParam(value = "count") Integer count,
                                  HttpServletRequest request) {
        List<Photo> photos = albumBean.getPhotos(idAlbum, page, count);
        constructUrl(request.getRequestURL().toString(), photos);
        return ResponseEntity.ok(convertToPhotoDto(photos));
    }

    //Get number photos in album
    @RequestMapping(value = "/albums/{id}/photosCnt", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity getCount(@PathVariable(value = "id") Long idAlbum) {
        return ResponseEntity.ok(albumBean.countPhotosInAlbum(idAlbum));
    }

    //Delete album
    @RequestMapping(value = "/photos/{id}", method = RequestMethod.DELETE)
    public ResponseEntity deletePhoto(@PathVariable("id") Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        try {
            albumBean.deletePhoto(userId, id);
            return ResponseEntity.ok().build();
        } catch (AccessDeniedException ex) {
            LOGGER.error(ex.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }

    private List<AlbumDto> convertToAlbumDto(List<Album> albums) {
        List<AlbumDto> result = new ArrayList<>();
        for (Album album : albums) {
            result.add(new AlbumDto(album));
        }
        return result;
    }

    private List<PhotoDto> convertToPhotoDto(List<Photo> photos) {
        List<PhotoDto> result = new ArrayList<>();
        for (Photo photo : photos) {
            result.add(new PhotoDto(photo));
        }
        return result;
    }
}
