package uk.co.informaticslab.molab3dwxds.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by tom on 26/05/2015.
 */
@Configuration
public class ServerConfiguration {

    @Value("${box.host-name}")
    public String serverName;

    @Value("${server.port}")
    public String serverPort;

    @Value("${server.context-path}")
    public String serverContext;

    public URI getServerUri() {
        try {
            return new URI("http://" + serverName + ":" + serverPort + serverContext);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
