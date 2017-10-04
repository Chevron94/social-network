package socialnetwork.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Roman on 26.08.2017 9:19.
 */
@Component
public class IpUpdater {

    private static final String CHECK_IP_HOSTNAME = "http://checkip.dyndns.org/";
    private static final String HOSTNAME = "hello-from.tk";
    private static final String TOKEN = "404158dc58c2d66e5248af03f5ce22f14e10eacba076fa058bd9bde2";
    private static final String MAIN_DNS_RECORD_ID = "39631818";
    private static final String WWW_DNS_RECORD_ID = "39631822";
    private static final String SUBDOMAIN = "www";

    private static final String REQUEST_MAIN = "https://pddimp.yandex.ru/nsapi/edit_a_record.xml?token="+TOKEN+"&domain="+HOSTNAME+"&record_id="+MAIN_DNS_RECORD_ID+"&content=";
    private static final String REQUEST_WWW = "https://pddimp.yandex.ru/nsapi/edit_a_record.xml?token="+TOKEN+"&domain="+HOSTNAME+"&subdomain="+SUBDOMAIN+"&record_id="+WWW_DNS_RECORD_ID+"&content=";

    private static String currentIp = "";

    @Scheduled(cron = "0 0/15 * * * *")
    public void updateIp() throws IOException {
        String ip =  getIP(CHECK_IP_HOSTNAME);
        if (!ip.equals(currentIp)) {
            currentIp = ip;
            sendRequests(REQUEST_MAIN, ip);
            sendRequests(REQUEST_WWW, ip);
        }
    }

    private void sendRequests(String request, String ip) throws IOException {
        URL url = new URL(request+ip);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.getInputStream();
        conn.disconnect();
    }

    private String getIP(String hostname) throws IOException {
        URL url = new URL(hostname);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        InputStream inputStream = conn.getInputStream();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return (response.toString().split(":")[1]).split("<")[0].trim();
    }
}
