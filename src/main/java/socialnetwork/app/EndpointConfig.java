package socialnetwork.app;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * Created by Roman on 23.08.2017 22:13.
 */
@ConditionalOnWebApplication
@Configuration
public class EndpointConfig {

    @Bean
    public ServerEndpointExporter endpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WebSocketsConfiguration customSpringConfigurator() {
        return new WebSocketsConfiguration(); // This is just to get context
    }
}
