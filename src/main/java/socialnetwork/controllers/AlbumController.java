package socialnetwork.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Роман on 31.03.2016.
 */
@Controller
public class AlbumController extends GenericController {
    private static final Logger LOGGER = Logger.getLogger(AlbumController.class);
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
        if (albums.size() == 0) {
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
    List<AlbumDto> loadMoreAlbums(
            @RequestParam(value = "idUser") Long idRequestUser,
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "count") Integer count,
            HttpServletRequest request) {
        List<Album> albums = albumBean.getAlbums(idRequestUser, page, count);
        for (Album album : albums) {
            constructUrl(request.getRequestURL().toString(), album.getPhotos());
        }
        return convertToAlbumDto(albums);

    }

    // Create new album
    @RequestMapping(value = "/albums", method = RequestMethod.POST)
    public @ResponseBody
    Long newAlbum(@RequestParam(value = "name") String name, HttpServletRequest request) {
        Long userId = getUserId(request);
        Album album = albumBean.createAlbum(userId, name);
        return album.getId();
    }

    //Delete album
    @RequestMapping(value = "/albums/{id}", method = RequestMethod.DELETE)
    public void deleteAlbum(@PathVariable("id") Long id, HttpServletRequest request) {
        Long userId =  getUserId(request);
        try {
            albumBean.deleteAlbum(userId, id);
        } catch (AccessDeniedException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/albums/{id}/rename", method = RequestMethod.POST)
    public void renameAlbum(@PathVariable("id") Long id, @RequestParam("newName") String newName, HttpServletRequest request) {
        Long userId = getUserId(request);
        try {
            albumBean.renameAlbum(userId, id, newName);
        } catch (AccessDeniedException | NameExistsException ex) {
            throw new BadRequestException(ex.getMessage());
        }
    }

    //Load more photos
    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public @ResponseBody
    List<PhotoDto> loadMorePhotos(@RequestParam(value = "idAlbum") Long idAlbum,
                                  @RequestParam(value = "page") Integer page,
                                  @RequestParam(value = "count") Integer count,
                                  HttpServletRequest request) {
        List<Photo> photos = albumBean.getPhotos(idAlbum, page, count);
        constructUrl(request.getRequestURL().toString(), photos);
        return convertToPhotoDto(photos);
    }

    //Get number photos in album
    @RequestMapping(value = "/albums/{id}/photosCnt", method = RequestMethod.GET)
    public @ResponseBody
    Long getCount(@PathVariable(value = "id") Long idAlbum) {
        return albumBean.countPhotosInAlbum(idAlbum);
    }

    //Delete album
    @RequestMapping(value = "/photos/{id}", method = RequestMethod.DELETE)
    public void deletePhoto(@PathVariable("id") Long id, HttpServletRequest request) {
        Long userId = getUserId(request);
        try {
            albumBean.deletePhoto(userId, id);
        } catch (AccessDeniedException ex) {
            LOGGER.error(ex.getMessage());
            throw new BadRequestException(ex.getMessage());
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
