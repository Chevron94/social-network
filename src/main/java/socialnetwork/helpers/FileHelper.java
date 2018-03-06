package socialnetwork.helpers;


import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import socialnetwork.exceptions.ValidationException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.UUID;

import static socialnetwork.helpers.Constants.FILES_PATH;

/**
 * Created by Роман on 30.01.2017.
 */
public class FileHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);

    public static String imageInputStreamToString(MultipartFile photo, Long userId, Long albumId) {

        try {
            InputStream inputStream = photo.getInputStream();
            BufferedImage image = ImageIO.read(inputStream);
            File directory = new File(FILES_PATH + File.separator + userId + File.separator + albumId);
            if (!directory.exists() && !directory.mkdirs()) {
                LOGGER.error("Failed to create directory " + directory.getPath());
                throw new RuntimeException("Failed to create directory " + directory.getPath());
            }
            try {
                File file = File.createTempFile(UUID.randomUUID().toString(), ".jpg", directory);
                Integer height = image.getHeight();
                Integer width = image.getWidth();
                float ratio = (float) height / (float) width;
                if (ratio > 1) {
                    image = Scalr.resize(image, Scalr.Method.QUALITY, Math.round(960f / ratio), 960);
                } else {
                    image = Scalr.resize(image, Scalr.Method.QUALITY, 1280, Math.round(1280f * ratio));
                }
                ImageIO.write(image, "jpg", file);
                return userId + "/" + albumId + "/" + file.getName();
            } catch (IOException e) {
                LOGGER.error("Can't read an image, ex: ", e);
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new ValidationException("File isn't an image");
        }
    }

    public static InputStream stringToInputStream(String path) {
        try {
            return new FileInputStream(FILES_PATH + File.separator + path);
        } catch (FileNotFoundException e) {
            LOGGER.error("ex: ", e);
            throw new RuntimeException(e);
        }
    }
}
