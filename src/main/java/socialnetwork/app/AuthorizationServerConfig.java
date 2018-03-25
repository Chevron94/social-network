package socialnetwork.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import socialnetwork.entities.oauth.ClientDetails;
import socialnetwork.repositories.ClientDetailsRepository;

import javax.sql.DataSource;

/**
 * Created by Roman on 10.02.2018 15:18.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ClientDetailsRepository clientDetailsRepository;

    @Value(value = "${client-id}")
    private String clientId;
    @Value(value = "${client-secret}")
    private String clientSecret;
    @Value(value = "${grant-types}")
    private String grantTypes;
    @Value(value = "${scope}")
    private String scope;
    @Value(value = "${redirect-url}")
    private String redirectUrl;
    @Value(value = "${resource-ids}")
    private String resourceIds;
    @Value(value = "${role}")
    private String role;
    @Value(value = "${access-token-validity-seconds}")
    private Integer accessTokenValiditySeconds;
    @Value(value = "${refresh-token-validity-seconds}")
    private Integer refreshTokenValiditySeconds;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(tokenStore());

    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);

        ClientDetails clientDetails = clientDetailsRepository.findOne(clientId);
        if (clientDetails == null) {
            clientDetails = new ClientDetails();
            clientDetails.setClientId(clientId);
            clientDetails.setClientSecret(clientSecret);
            clientDetails.setAccessTokenValidity(accessTokenValiditySeconds);
            clientDetails.setRefreshTokenValidity(refreshTokenValiditySeconds);
            clientDetails.setAuthorizedGrantTypes(grantTypes);
            clientDetails.setResourceIds(resourceIds);
            clientDetails.setScope(scope);
            clientDetails.setAuthorities(role);
            clientDetails.setWebServerRedirectUri(redirectUrl);
            clientDetailsRepository.save(clientDetails);
        }
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

}
