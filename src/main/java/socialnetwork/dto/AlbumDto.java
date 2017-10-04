package socialnetwork.dto;

import socialnetwork.entities.Album;
import socialnetwork.entities.Photo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Roman on 24.08.2017 19:30.
 */
public class AlbumDto {
    private Long id;
    private String name;
    private Date creationDate;
    private Long userId;
    private List<PhotoDto> photos;

    public AlbumDto() {
    }

    public AlbumDto(Album album){
        this.id = album.getId();
        this.name = album.getName();
        this.creationDate = album.getCreationDate();
        this.userId = album.getUser().getId();
        this.photos = convert(album.getPhotos());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PhotoDto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotoDto> photos) {
        this.photos = photos;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    private List<PhotoDto> convert(List<Photo> photos){
        List<PhotoDto> photoDtos = new ArrayList<>();
        for (Photo photo: photos){
            photoDtos.add(new PhotoDto(photo));
        }
        return photoDtos;
    }

    @Override
    public String toString() {
        return "AlbumDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", userId=" + userId +
                ", photos=" + photos +
                '}';
    }
}
