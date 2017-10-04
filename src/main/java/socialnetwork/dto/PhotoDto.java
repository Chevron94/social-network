package socialnetwork.dto;

import socialnetwork.entities.Photo;

import java.util.Date;

/**
 * Created by Roman on 24.08.2017 19:31.
 */
public class PhotoDto {
    private Long id;
    private String photoUrl;
    private Date uploaded;
    private String definition;
    private Long albumId;
    private Long userId;

    public PhotoDto() {
    }

    public PhotoDto(Photo photo){
        this.id = photo.getId();
        this.photoUrl = photo.getPhotoUrl();
        this.uploaded = photo.getUploaded();
        this.definition = photo.getDefinition();
        this.albumId = photo.getAlbum().getId();
        this.userId = photo.getAlbum().getUser().getId();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Date getUploaded() {
        return uploaded;
    }

    public void setUploaded(Date uploaded) {
        this.uploaded = uploaded;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public Long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "PhotoDto{" +
                "id=" + id +
                ", photoUrl='" + photoUrl + '\'' +
                ", uploaded=" + uploaded +
                ", definition='" + definition + '\'' +
                ", albumId=" + albumId +
                ", userId=" + userId +
                '}';
    }
}
