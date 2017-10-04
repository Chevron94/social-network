package socialnetwork.helpers;

import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static socialnetwork.helpers.Constants.EMAIL_LOGIN;
import static socialnetwork.helpers.Constants.EMAIL_PASSWORD;
import static socialnetwork.helpers.Constants.SMTP_HOST;

/**
 * Created by Roman on 22.08.2017 21:20.
 */
public class EmailHelper {
    private static final Logger LOGGER = Logger.getLogger(EmailHelper.class);
    public static void sendMail(String receiver, String topic, String text) {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(EMAIL_LOGIN, EMAIL_PASSWORD);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_LOGIN));
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(topic);
            message.setText(text);
            Transport.send(message);
        } catch (MessagingException ex) {
            LOGGER.error(ex);
        }
    }
}
