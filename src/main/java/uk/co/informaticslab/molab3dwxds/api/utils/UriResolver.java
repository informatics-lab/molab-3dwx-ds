package uk.co.informaticslab.molab3dwxds.api.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
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

    private static final DateTimeFormatter DTF = ISODateTimeFormat.dateTime().withZoneUTC();

    @Autowired
    public ServerConfiguration serverConfiguration;

    public URI mkUriForClass(final Class resourceClass) {
        URI href = UriBuilder.fromResource(resourceClass).build();
        return URI.create(getBaseUriString()).resolve(href);
    }

    public String getBaseUriString() {
        return serverConfiguration.getServerUri() + "/";
    }

    public URI mkUri(Object... paths) {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseUriString());
        for (Object path : paths) {
            if (path instanceof DateTime) {
                sb.append(DTF.print((DateTime) path));
            } else {
                sb.append(path.toString());
            }
            sb.append("/");
        }
        sb.deleteCharAt(sb.lastIndexOf("/"));
        return URI.create(sb.toString());
    }
}
