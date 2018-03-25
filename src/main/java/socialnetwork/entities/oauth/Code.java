package socialnetwork.entities.oauth;

import javax.persistence.*;

/**
 * Created by Roman on 10.03.2018 13:01.
 */
@Entity
@Table(name = "oauth_code")
public class Code {
    @Column(name = "code")
    private String code;
    @Id
    @Column(name = "authentication")
    private Byte[] authentication;

    public Code() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Byte[] authentication) {
        this.authentication = authentication;
    }

    @Override
    public String toString() {
        return "Code{" +
                "code='" + code + '\'' +
                ", authentication=" + authentication +
                '}';
    }
}