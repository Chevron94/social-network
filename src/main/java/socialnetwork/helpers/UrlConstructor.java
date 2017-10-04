package socialnetwork.helpers;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by Roman on 19.08.2017.
 */
public class UrlConstructor {
    public static String constructUrl(String requestUrl, String path) {
        try {
            URL url = new URL(requestUrl);
            String host = url.getHost();
            String userInfo = url.getUserInfo();
            String scheme = url.getProtocol();
            Integer port = url.getPort();
            return new URI(scheme, userInfo, host, port, path, null, null).toString();
        } catch (URISyntaxException | MalformedURLException ex) {
            //Incorrect URL, btw I hope It will newer happen
            return "";
        }
    }

    public static String getCurrentUrl(String requestUrl){
        return constructUrl(requestUrl, null);
    }
}
