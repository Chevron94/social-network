package socialnetwork.helpers;

import socialnetwork.dto.AlbumDto;
import socialnetwork.dto.PhotoDto;
import socialnetwork.dto.VocabularyDto;
import socialnetwork.entities.Album;
import socialnetwork.entities.Photo;
import socialnetwork.entities.Vocabulary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roman on 06.03.2018 12:32.
 */
public class Converters {
    public static List<AlbumDto> convertAlbumsToAlbumDtos(List<Album> albums) {
        List<AlbumDto> result = new ArrayList<>();
        for (Album album : albums) {
            result.add(new AlbumDto(album));
        }
        return result;
    }

    public static List<PhotoDto> convertPhotosToPhotoDtos(List<Photo> photos) {
        List<PhotoDto> result = new ArrayList<>();
        for (Photo photo : photos) {
            result.add(new PhotoDto(photo));
        }
        return result;
    }

    public static List<VocabularyDto> convertToVocabularyDtos(List<Vocabulary> vocabularies) {
        List<VocabularyDto> result = new ArrayList<>();
        for (Vocabulary vocabulary : vocabularies) {
            result.add(new VocabularyDto(vocabulary));
        }
        return result;
    }
}
