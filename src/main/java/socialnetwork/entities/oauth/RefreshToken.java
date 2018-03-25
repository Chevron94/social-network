package socialnetwork.entities.oauth;

import javax.persistence.*;

/**
 * Created by Roman on 10.03.2018 13:01.
 */
@Entity
@Table(name = "oauth_refresh_token")
public class RefreshToken {
    @Id
    @Column(name = "token_id")
    private String tokenId;
    @Column(name = "token")
    private Byte[] token;
    @Column(name = "authentication")
    private Byte[] authentication;

    public RefreshToken() {
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Byte[] getToken() {
        return token;
    }

    public void setToken(Byte[] token) {
        this.token = token;
    }

    public Byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Byte[] authentication) {
        this.authentication = authentication;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "tokenId='" + tokenId + '\'' +
                ", token=" + token +
                ", authentication=" + authentication +
                '}';
    }
}