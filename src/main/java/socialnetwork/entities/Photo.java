package socialnetwork.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by roman on 22.09.15.
 */
@Entity
@Table(name = "PHOTO")
public class Photo {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "PHOTO_URL")
    private String photoUrl;

    @Column(name = "UPLOADED")
    private Date uploaded;

    @Column(name = "DEFINITION")
    private String definition;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ALBUM_ID", nullable = false)
    private Album album;

    public Photo() {
    }

    public Photo(String photoUrl, Date uploaded, Album album) {
        this.photoUrl = photoUrl;
        this.uploaded = uploaded;
        this.album = album;
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

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        return !(id != null ? !id.equals(photo.id) : photo.id != null);

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

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", photoUrl='" + photoUrl + '\'' +
                ", album=" + album +
                '}';
    }
}
