package socialnetwork.security.impl;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import socialnetwork.beans.UserBean;
import socialnetwork.entities.User;
import socialnetwork.exceptions.AccessDeniedException;
import socialnetwork.security.TokenService;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Roman on 27.01.2018 9:34.
 */
@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private UserBean userBean;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String getToken(String username, String password) {
        User user = userBean.getUser(username);
        if (passwordEncoder.matches(password, user.getPassword())){
            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("clientType", "user");
            tokenData.put("userID", user.getId().toString());
            tokenData.put("username", user.getLogin());
            tokenData.put("token_create_date", new Date().getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, 3);
            tokenData.put("token_expiration_date", calendar.getTime());
            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setExpiration(calendar.getTime());
            jwtBuilder.setClaims(tokenData);
            String key = "abc123";
            return jwtBuilder.signWith(SignatureAlgorithm.HS512, key).compact();
        } else {
            throw new AccessDeniedException("Incorrect login or password");
        }
    }
}
