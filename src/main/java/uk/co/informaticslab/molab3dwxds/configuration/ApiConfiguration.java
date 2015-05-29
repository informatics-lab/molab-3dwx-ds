package uk.co.informaticslab.molab3dwxds.configuration;

import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;

/**
 * Created by tom on 15/05/2015.
 */
@Configuration
public class ApiConfiguration {

    @Bean
    public RepresentationFactory getRepresentationFactory() {
        RepresentationFactory representationFactory = new MyRepresentationFactory()
                .withFlag(RepresentationFactory.PRETTY_PRINT)
                .withFlag(RepresentationFactory.STRIP_NULLS);

        return representationFactory;
    }

}
