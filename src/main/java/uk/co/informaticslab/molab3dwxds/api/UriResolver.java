package uk.co.informaticslab.molab3dwxds.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.informaticslab.molab3dwxds.configuration.ServerConfiguration;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by tom on 26/05/2015.
 */
@Component
public class UriResolver {

    @Autowired
    public ServerConfiguration serverConfiguration;

    public URI mkUriForClass(final Class resourceClass) {
        URI href = UriBuilder.fromResource(resourceClass).build();
        return URI.create(getBaseUriString()).resolve(href);
    }

    public String getBaseUriString() {
        return serverConfiguration.getServerUri() + "/";
    }
}
