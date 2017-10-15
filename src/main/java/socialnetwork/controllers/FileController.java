package socialnetwork.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import socialnetwork.helpers.FileHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by Роман on 30.01.2017.
 */
@Controller
public class FileController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);
    @RequestMapping(value = "/images/{userid}/{album}/{photo:.+}", method = RequestMethod.GET)
    public void getFile(@PathVariable("userid") String userid, @PathVariable("album") String album, @PathVariable("photo") String photo, HttpServletResponse response) {
        InputStream inputStream = null;
        try {
            inputStream = FileHelper.stringToInputStream(userid+File.separator+album+File.separator+photo);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    LOGGER.error("Can't close channel, ex:", e1);
                }
            }
        }
    }
}
