package socialnetwork.beans.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import socialnetwork.beans.GraphicsBean;
import socialnetwork.dto.*;

import java.util.*;

/**
 * Created by Roman on 26.04.2018 1:11.
 */
@Component
public class GraphicsBeanImpl implements GraphicsBean {

    private static final String WITH_FRIENDS =
            "WITH friends AS (" +
                    "SELECT receiver_id AS id " +
                    "FROM friend_request f " +
                    "WHERE f.sender_id = :userID AND f.confirmed = true\n" +
                    "UNION\n" +
                    "SELECT sender_id AS id " +
                    "FROM friend_request f " +
                    "WHERE f.receiver_id = :userID AND f.confirmed = true)\n";

    private static final String WITH_COUNTRY_FRIENDS =
            "WITH friends AS (" +
                    "SELECT DISTINCT receiver_id AS id " +
                    "FROM friend_request f, network_user u " +
                    "WHERE f.sender_id = u.id AND f.confirmed = true AND u.country_id = :countryID\n" +
                    "UNION\n" +
                    "SELECT DISTINCT sender_id AS id " +
                    "FROM friend_request f " +
                    "WHERE f.receiver_id = :countryID AND f.confirmed = true)\n";

    private static final String FRIENDS_COUNTRIES_SQL =
            "SELECT c.name AS name, COUNT(u) AS value " +
                    "FROM network_user u, friends f, country c " +
                    "WHERE u.id = f.id AND c.id = u.country_id " +
                    "GROUP BY c.name " +
                    "ORDER BY value DESC;";

    private static final String COUNTRY_FRIENDS_STAT_SQL =
            WITH_COUNTRY_FRIENDS + FRIENDS_COUNTRIES_SQL;

    private static final String FRIENDS_STAT_SQL =
            WITH_FRIENDS + FRIENDS_COUNTRIES_SQL;


    private static final String USERS_BY_COUNTRY_STAT_SQL =
            "SELECT c.name AS name, count(u) AS value " +
                    "FROM country c, network_user u " +
                    "WHERE c.id = u.country_id " +
                    "GROUP BY c.name " +
                    "ORDER BY value DESC;";

    private static final String USERS_BY_NATIVE_LANGUAGE_SQL =
            "SELECT lang.name AS name, count(*) AS value \n" +
                    "FROM language AS lang, language_user AS lu\n" +
                    "WHERE lu.language_id = lang.id\n" +
                        "AND lu.language_level_id = 8\n" +
                    "GROUP BY lang.name\n" +
                    "ORDER BY value desc";

    private static final String USERS_BY_LEARNING_LANGUAGE_SQL =
            "SELECT lang.name AS name, count(*) AS value \n" +
                    "FROM language AS lang, language_user AS lu\n" +
                    "WHERE lu.language_id = lang.id\n" +
                        "AND lu.language_level_id < 8\n" +
                    "GROUP BY lang.name\n" +
                    "ORDER BY value desc";

    private static final String FRIENDS_BY_LEARNING_LANGUAGE_SQL =
            WITH_FRIENDS +
                    "SELECT lang.name AS name, count(*) AS value \n" +
                    "FROM language AS lang, language_user AS lu,  friends f\n" +
                    "WHERE lu.language_id = lang.id\n" +
                        "AND lu.user_id = f.id\n" +
                        "AND lu.language_level_id < 8\n" +
                    "GROUP BY lang.name\n" +
                    "ORDER BY value desc";

    private static final String FRIENDS_BY_NATIVE_LANGUAGE_SQL =
            WITH_FRIENDS +
                    "SELECT lang.name AS name, count(*) AS value \n" +
                    "FROM language AS lang, language_user AS lu,  friends f\n" +
                    "WHERE lu.language_id = lang.id\n" +
                        "AND lu.user_id = f.id\n" +
                        "AND lu.language_level_id = 8\n" +
                    "GROUP BY lang.name\n" +
                    "ORDER BY value desc";

    private static final String SPECIFIC_COUNTRY_USERS_BY_LEARNING_LANGUAGE_SQL =
                    "SELECT lang.name AS name, count(*) AS value \n" +
                    "FROM language AS lang, language_user AS lu,  network_users u\n" +
                    "WHERE lu.language_id = lang.id\n" +
                        "AND lu.user_id = u.id\n" +
                        "AND u.country_id = :countryID\n" +
                        "AND lu.language_level_id < 8\n" +
                    "GROUP BY lang.name\n" +
                    "ORDER BY value desc";

    private static final String SPECIFIC_COUNTRY_USERS_BY_NATIVE_LANGUAGE_SQL =
                    "SELECT lang.name AS name, count(*) AS value \n" +
                    "FROM language AS lang, language_user AS lu,  network_users u\n" +
                    "WHERE lu.language_id = lang.id\n" +
                        "AND lu.user_id = u.id\n" +
                        "AND u.country_id = :countryID\n" +
                        "AND lu.language_level_id = 8\n" +
                    "GROUP BY lang.name\n" +
                    "ORDER BY value desc";

    private static final String SENT_MESSAGES_BY_FRIENDS_PER_SPECIFIC_USER_SQL =
            WITH_FRIENDS +
                    "SELECT u.name || '(' || u.login || ')' AS friend_name, cast(m.dateTime AS date) AS date, count(*) \n" +
                    "FROM user_dialog ud, user_dialog fr, friends f, dialog d, message m, network_user u\n" +
                    "WHERE ud.user_id = :userID \n" +
                        "AND f.id = fr.user_id \n" +
                        "AND ud.dialog_id = fr.dialog_id \n" +
                        "AND d.id = ud.dialog_id \n" +
                        "AND m.dialog_id = d.id \n" +
                        "AND m.user_id = :userID \n" +
                        "AND u.id = f.id\n" +
                    "GROUP BY friend_name, date\n" +
                    "ORDER BY date ASC;";

    private static final String RECEIVED_MESSAGES_BY_FRIENDS_PER_SPECIFIC_USER_SQL =
            WITH_FRIENDS +
                    "SELECT u.name || '(' || u.login || ')' AS friend_name, cast(m.dateTime AS date) AS date, count(*) \n" +
                    "FROM user_dialog ud, user_dialog fr, friends f, dialog d, message m, network_user u\n" +
                    "WHERE ud.user_id = :userID \n" +
                    "AND f.id = fr.user_id \n" +
                    "AND ud.dialog_id = fr.dialog_id \n" +
                    "AND d.id = ud.dialog_id \n" +
                    "AND m.dialog_id = d.id \n" +
                    "AND m.user_id = f.id \n" +
                    "AND u.id = f.id\n" +
                    "GROUP BY friend_name, date\n" +
                    "ORDER BY date ASC;";

    private static final String SENT_MESSAGES_BY_COUNTRY_PER_SPECIFIC_USER_SQL =
            WITH_FRIENDS +
                    "SELECT c.name AS country_name, cast(m.dateTime AS date) AS date, count(*) \n" +
                    "FROM user_dialog ud, user_dialog fr, friends f, dialog d, message m, network_user u, country c\n" +
                    "WHERE ud.user_id = :userID \n" +
                    "AND f.id = fr.user_id \n" +
                    "AND ud.dialog_id = fr.dialog_id \n" +
                    "AND d.id = ud.dialog_id \n" +
                    "AND m.dialog_id = d.id \n" +
                    "AND m.user_id = :userID \n" +
                    "AND u.id = f.id\n" +
                    "AND c.id = u.country_id\n" +
                    "GROUP BY country_name, date\n" +
                    "ORDER BY date ASC;";

    private static final String RECEIVED_MESSAGES_BY_COUNTRY_PER_SPECIFIC_USER_SQL =
            WITH_FRIENDS +
                    "SELECT c.name AS country_name, cast(m.dateTime AS date) AS date, count(*) \n" +
                    "FROM user_dialog ud, user_dialog fr, friends f, dialog d, message m, network_user u, country c\n" +
                    "WHERE ud.user_id = :userID \n" +
                    "AND f.id = fr.user_id \n" +
                    "AND ud.dialog_id = fr.dialog_id \n" +
                    "AND d.id = ud.dialog_id \n" +
                    "AND m.dialog_id = d.id \n" +
                    "AND m.user_id = f.id \n" +
                    "AND u.id = f.id\n" +
                    "AND c.id = u.country_id\n" +
                    "GROUP BY country_name, date\n" +
                    "ORDER BY date ASC;";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;



    @Override
    public List<PieGraphicDTO> getFriendsByCountryPerSpecificCountryDiagram(Long countryId) {
        return Collections.singletonList(buildPieGraphicDTO("friends", COUNTRY_FRIENDS_STAT_SQL, Collections.singletonMap("countryID", countryId)));
    }

    @Override
    public List<PieGraphicDTO> getFriendsByCountryPerUserDiagram(Long userId) {
        return Collections.singletonList(buildPieGraphicDTO("friends", FRIENDS_STAT_SQL, Collections.singletonMap("userID", userId)));
    }

    @Override
    public List<PieGraphicDTO> getUsersByCountryDiagram() {
        return Collections.singletonList(buildPieGraphicDTO("users", USERS_BY_COUNTRY_STAT_SQL, Collections.emptyMap()));
    }

    private PieGraphicDTO buildPieGraphicDTO(String seriesName, String sql, Map<String, Object> parameters){
        PieGraphicDTO pieGraphicDTO = new PieGraphicDTO();
        pieGraphicDTO.setName(seriesName);
        if (parameters == null){
            parameters = Collections.emptyMap();
        }
        List<PieSeriesDTO> pieSeries = namedParameterJdbcTemplate.query(sql, parameters, resultSet -> {
            List<PieSeriesDTO> result = new ArrayList<>();
            while (resultSet.next()){
                PieSeriesDTO pieSeriesDTO = new PieSeriesDTO();
                pieSeriesDTO.setName(resultSet.getString("name"));
                pieSeriesDTO.setValue(resultSet.getInt("value"));
                result.add(pieSeriesDTO);
            }
            return result;
        });
        pieGraphicDTO.setData(pieSeries);
        return pieGraphicDTO;
    }

    private BlockGraphicDTO buildBlockGraphicDTO(List<String> categories, String firstSQL, String secondSQL, Map<String, Object> parameters){
        BlockGraphicDTO blockGraphicDTO = new BlockGraphicDTO();
        blockGraphicDTO.setCategories(categories);
        if (parameters == null){
            parameters = Collections.emptyMap();
        }
        Map<String, Integer> first = new HashMap<>();
        Map<String, Integer> second = new HashMap<>();
        namedParameterJdbcTemplate.query(firstSQL, parameters, resultSet -> {
            while (resultSet.next()){
                first.put(resultSet.getString("name"), resultSet.getInt("value"));
            }
        });
        namedParameterJdbcTemplate.query(secondSQL, parameters, resultSet -> {
            while (resultSet.next()){
                second.put(resultSet.getString("name"), resultSet.getInt("value"));
            }
        });
        List<BlockSeriesDTO> blockSeries = new ArrayList<>();
        for (Map.Entry<String, Integer> entry: first.entrySet()){
            BlockSeriesDTO blockSeriesDTO = new BlockSeriesDTO();
            blockSeriesDTO.setName(entry.getKey());
            Integer secondSeriesValue = second.getOrDefault(entry.getKey(), 0);
            second.remove(entry.getKey());
            blockSeriesDTO.setValues(Arrays.asList(entry.getValue(), secondSeriesValue));
            blockSeries.add(blockSeriesDTO);
        }
        if (!second.isEmpty()){
            for (Map.Entry<String, Integer> entry: second.entrySet()){
                BlockSeriesDTO blockSeriesDTO = new BlockSeriesDTO();
                blockSeriesDTO.setName(entry.getKey());
                blockSeriesDTO.setValues(Arrays.asList(0, entry.getValue()));
                blockSeries.add(blockSeriesDTO);
            }
        }
        blockGraphicDTO.setSeries(blockSeries);
        return blockGraphicDTO;
    }

    private LineGraphicDTO buildLineGraphicDTO(String sql, Map<String, Object> parameters) {
        LineGraphicDTO lineGraphicDTO = new LineGraphicDTO();
        if (parameters == null){
            parameters = Collections.emptyMap();
        }
        List<LineSeriesDTO> series = namedParameterJdbcTemplate.query(sql, parameters, resultSet -> {
            List<LineSeriesDTO> result = new ArrayList<>();
            while (resultSet.next()){
                LineSeriesDTO seriesDTO = new LineSeriesDTO();
                seriesDTO.setName(resultSet.getString("name"));
                seriesDTO.setType("area");

            }
            return result;
        });
        lineGraphicDTO.setSeries(series);
        return lineGraphicDTO;
    }

    //todo: IMPLEMENT METHODS!!!!

    @Override
    public List<PieGraphicDTO> getSentMessagesByCountryPerUserDiagram(Long userID) {
        return null;
    }

    @Override
    public List<PieGraphicDTO> getReceivedMessagesByCountryPerUserDiagram(Long userID) {
        return null;
    }

    @Override
    public List<PieGraphicDTO> getSentMessagesByFriendsPerUserDiagram(Long userID) {
        return null;
    }

    @Override
    public List<PieGraphicDTO> getReceivedMessagesByFriendsPerUserDiagram(Long userID) {
        return null;
    }

    @Override
    public List<PieGraphicDTO> getSentMessagesByCountryDiagram() {
        return null;
    }

    @Override
    public List<PieGraphicDTO> getReceivedMessagesByCountryDiagram() {
        return null;
    }

    @Override
    public BlockGraphicDTO getFriendsByLanguagesPerUserDiagram(Long userId) {
        return null;
    }

    @Override
    public BlockGraphicDTO getUsersByLanguagesDiagram() {
        return null;
    }

    @Override
    public BlockGraphicDTO getUsersByLanguagesPerSpecificCountryDiagram(Long countryId) {
        return null;
    }

    @Override
    public LineGraphicDTO getMessagesByCountriesPerUserDiagram(Long userId) {
        return null;
    }

    @Override
    public LineGraphicDTO getMessagesByFriendsPerUserDiagram(Long userId) {
        return null;
    }

    @Override
    public LineGraphicDTO getMessagesByCountriesDiagram() {
        return null;
    }
}
