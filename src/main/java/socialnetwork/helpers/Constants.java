package socialnetwork.helpers;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by Роман on 30.01.2017.
 */
public class Constants {
    @Value("hellofrom.constants.host-url")
    public static final String HOST_URL = "https://hello-from.tk";
    @Value("hellofrom.constants.files-path")
    public static final String FILES_PATH = "C:\\Users\\Roman\\hellofrom\\photos";
    @Value("hellofrom.constants.email-login")
    public static final String EMAIL_LOGIN = "no-reply@hello-from.tk";
    @Value("hellofrom.constants.email-password")
    public static final String EMAIL_PASSWORD = "Chevron94Vrn";
    @Value("hellofrom.constants.smtp-host")
    public static final String SMTP_HOST = "smtp.yandex.ru";
    public static final String DEFAULT_PHOTO = "/resources/images/system/no-photo.png";

    public Constants() {
    }
}
