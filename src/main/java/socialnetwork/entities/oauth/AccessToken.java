package socialnetwork.entities.oauth;

import javax.persistence.*;

/**
 * Created by Roman on 10.03.2018 13:00.
 */
@Entity
@Table(name = "oauth_access_token")
public class AccessToken {
    @Id
    @Column(name = "authentication_id")
    private String authenticationId;
    @Column(name = "token_id")
    private String tokenId;
    @Column(name = "token")
    private Byte[] token;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "client_id")
    private String clientId;
    @Column(name = "authentication")
    private Byte[] authentication;
    @Column(name = "refresh_token")
    private String refreshToken;

    public AccessToken() {
    }

    public String getAuthenticationId() {
        return authenticationId;
    }

    public void setAuthenticationId(String authenticationId) {
        this.authenticationId = authenticationId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Byte[] authentication) {
        this.authentication = authentication;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "authenticationId='" + authenticationId + '\'' +
                ", tokenId='" + tokenId + '\'' +
                ", token=" + token +
                ", userName='" + userName + '\'' +
                ", clientId='" + clientId + '\'' +
                ", authentication=" + authentication +
                ", refreshToken='" + refreshToken + '\'' +
                '}';
    }
}