package uk.co.informaticslab.molab3dwxds.configuration;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import uk.co.informaticslab.molab3dwxds.api.binders.DateTimeBinder;
import uk.co.informaticslab.molab3dwxds.api.filters.CorsResponseFilter;
import uk.co.informaticslab.molab3dwxds.api.providers.DefaultExceptionMapper;

/**
 * Configures and registers the endpoints & providers for jersey
 */
@Configuration
public class JerseyConfiguration extends ResourceConfig {

    public JerseyConfiguration() {

        //allows for multipart form data to be consumed
        register(MultiPartFeature.class);

        register(new DateTimeBinder());

        register(DefaultExceptionMapper.class);

        register(CorsResponseFilter.class);

        //registers the jaxrs support
        packages("com.theoryinpractise.halbuilder.jaxrs");

        //scans our controllers package and register them for us
        packages("uk.co.informaticslab.molab3dwxds.api.controllers");

    }

}
