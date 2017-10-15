package socialnetwork.beans.impl;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import socialnetwork.beans.AlbumBean;
import socialnetwork.entities.Album;
import socialnetwork.entities.Photo;
import socialnetwork.exceptions.AccessDeniedException;
import socialnetwork.exceptions.NameExistsException;
import socialnetwork.helpers.FileHelper;
import socialnetwork.helpers.StringHelper;
import socialnetwork.repositories.AlbumRepository;
import socialnetwork.repositories.PhotoRepository;
import socialnetwork.repositories.UserRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Roman on 09.08.2017.
 */
@Component
public class AlbumBeanImpl implements AlbumBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlbumBeanImpl.class);
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public Album createAlbum(Long userId, String albumName) {
        albumName = StringHelper.replaceSymbols(albumName);
        Album album = albumRepository.findAlbumByUser_IdAndName(userId, albumName);
        if (album == null) {
            album = new Album(albumName, userRepository.findOne(userId), new Date());
            return albumRepository.save(album);
        } else {
            throw new NameExistsException("User with id: " + userId + " already has album with name: " + albumName);
        }
    }

    @Override
    @Transactional
    public Album renameAlbum(Long userId, Long albumId, String newName) {
        Album album = albumRepository.findOne(albumId);
        newName = StringHelper.replaceSymbols(newName);
        if (!album.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User with id: " + userId + "isn't owner of the album with id: " + albumId);
        }
        if (albumRepository.findAlbumByUser_IdAndName(userId, newName) == null) {
            album.setName(newName);
            album = albumRepository.save(album);
            return album;
        } else {
            throw new NameExistsException("User with id: " + userId + " already has album with name: " + newName);
        }
    }

    @Override
    public Album getAlbum(Long albumId) {
        return albumRepository.findOne(albumId);
    }

    @Override
    public Album getAlbum(Long userId, String albumName) {
        return albumRepository.findAlbumByUser_IdAndName(userId, albumName);
    }

    @Override
    public List<Album> getAlbums(Long userId, Integer page, Integer count) {
        List<Album> albums = albumRepository.findAlbumsByUser_IdOrderByCreationDateDesc(userId, new PageRequest(page, count));
        for (Album a : albums) {
            a.setPhotos(photoRepository.findPhotosByAlbum_IdOrderByUploadedDesc(a.getId(), new PageRequest(0, 6)));
        }
        return albums;
    }

    @Override
    @Transactional
    public void deleteAlbum(Long userId, Long albumId) {
        Album album = albumRepository.findOne(albumId);
        if (!album.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User with id: " + userId + "isn't owner of the album with id: " + albumId);
        }
        photoRepository.delete(photoRepository.countPhotosByAlbum_Id(albumId));
        albumRepository.delete(albumId);
    }

    @Override
    @Transactional
    public Album storePhotos(Long userId, Long albumId, List<MultipartFile> images) {
        Album album = albumRepository.findOne(albumId);
        if (!album.getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User with id: " + userId + "isn't owner of the album with id: " + albumId);
        }
        List<Photo> photos = new ArrayList<>();
        for (MultipartFile uploadedPhoto : images) {
            photos.add(new Photo("/images/" + FileHelper.imageInputStreamToString(uploadedPhoto, userId, album.getId()), new Date(), album));

        }
        photos = photoRepository.save(photos);
        //return photos;
        return album;
    }

    @Override
    public Photo getPhoto(Long photoId) {
        return photoRepository.findOne(photoId);
    }

    @Override
    public List<Photo> getPhotos(Long albumId, Integer page, Integer count) {
        return photoRepository.findPhotosByAlbum_IdOrderByUploadedDesc(albumId, new PageRequest(page, count));
    }

    @Override
    public Long countPhotosInAlbum(Long albumId) {
        return photoRepository.countPhotosByAlbum_Id(albumId);
    }

    @Override
    public Long countAlbums(Long userId) {
        return albumRepository.countAlbumsByUser_Id(userId);
    }

    @Override
    @Transactional
    public void copyPhotoToAlbum(Long userId, Long photoId, Long destinationAlbumId) {
        Photo sourcePhoto = photoRepository.findOne(photoId);
        if (sourcePhoto.getAlbum().getUser().getId().equals(userId)) {
            Album destinationAlbum = albumRepository.findOne(destinationAlbumId);
            if (destinationAlbum.getUser().getId().equals(userId)) {
                Photo photo = new Photo(sourcePhoto.getPhotoUrl(), new Date(), destinationAlbum);
                photoRepository.save(photo);
            } else {
                throw new AccessDeniedException("User with id: "+userId+" isn't owner of the album with id: "+destinationAlbumId);
            }
        } else {
            throw new AccessDeniedException("User with id: "+userId+" isn't owner of the photo with id: "+photoId);
        }
    }

    @Override
    @Transactional
    public void deletePhoto(Long userId, Long photoId) {
        Photo photo = photoRepository.findOne(photoId);
        if (!photo.getAlbum().getUser().getId().equals(userId)) {
            throw new AccessDeniedException("User with id: " + userId + "isn't owner of the photo with id: " + photoId);
        }
        photoRepository.delete(photo);
    }
}
