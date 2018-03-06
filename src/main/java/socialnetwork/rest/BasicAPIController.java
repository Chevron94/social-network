package socialnetwork.rest;

/**
 * Created by Roman on 10.02.2018 14:22.
 */

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/test")
public class BasicAPIController {
    @RequestMapping(method = RequestMethod.GET)
    public String test(){
        return "It works";
    }
}
