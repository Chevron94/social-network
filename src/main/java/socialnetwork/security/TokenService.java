package socialnetwork.security;

/**
 * Created by Roman on 27.01.2018 9:38.
 */
public interface TokenService {
    String getToken(String username, String password);
}
