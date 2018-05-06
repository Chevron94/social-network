package socialnetwork.beans;

import socialnetwork.dto.BlockGraphicDTO;
import socialnetwork.dto.LineGraphicDTO;
import socialnetwork.dto.PieGraphicDTO;

import java.util.List;

/**
 * Created by Roman on 26.04.2018 1:00.
 */
public interface GraphicsBean {
    //Pie diagrams Personal
    List<PieGraphicDTO> getSentMessagesByCountryPerUserDiagram(Long userID);
    List<PieGraphicDTO> getReceivedMessagesByCountryPerUserDiagram(Long userID);
    List<PieGraphicDTO> getSentMessagesByFriendsPerUserDiagram(Long userID);
    List<PieGraphicDTO> getReceivedMessagesByFriendsPerUserDiagram(Long userID);
    List<PieGraphicDTO> getFriendsByCountryPerUserDiagram(Long userId);
    //Pie Diagram General
    List<PieGraphicDTO> getSentMessagesByCountryDiagram();
    List<PieGraphicDTO> getReceivedMessagesByCountryDiagram();
    List<PieGraphicDTO> getUsersByCountryDiagram();
    //Pie diagram Country-specific
    List<PieGraphicDTO> getFriendsByCountryPerSpecificCountryDiagram(Long countryId);

    //Block diagram Personal
    BlockGraphicDTO getFriendsByLanguagesPerUserDiagram(Long userId);
    //Block diagram General
    BlockGraphicDTO getUsersByLanguagesDiagram();
    //Block diagram Country-specific
    BlockGraphicDTO getUsersByLanguagesPerSpecificCountryDiagram(Long countryId);

    //Line diagram Personal
    LineGraphicDTO getMessagesByCountriesPerUserDiagram(Long userId);
    LineGraphicDTO getMessagesByFriendsPerUserDiagram(Long userId);
    //Line diagram General
    LineGraphicDTO getMessagesByCountriesDiagram();
}
