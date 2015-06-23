package uk.co.informaticslab.molab3dwxds.api.representations;

import com.theoryinpractise.halbuilder.DefaultRepresentationFactory;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.json.JsonRepresentationReader;
import uk.co.informaticslab.molab3dwxds.domain.Constants;
import uk.co.informaticslab.molab3dwxds.domain.Image;
import uk.co.informaticslab.molab3dwxds.domain.Video;

import java.net.URI;

/**
 * Custom representation factory for JSON allows us to inject our own renderer/reader.
 */
public class MyRepresentationFactory extends DefaultRepresentationFactory {

    public MyRepresentationFactory() {
        withRenderer(HAL_JSON, MyJsonRepresentationWriter.class);
        withReader(HAL_JSON, JsonRepresentationReader.class);
    }

    public Representation getImageAsRepresentation(URI self, Image image) {
        String selfHref = self + "/" + image.getId();
        Representation repr = this.newRepresentation(selfHref);
        repr.withLink(Constants.DATA, selfHref + "/data");
        repr.withProperty(Constants.RESOLUTION, image.getResolution());
        repr.withProperty(Constants.MODEL, image.getModel());
        repr.withProperty(Constants.FORECAST_REFERENCE_TIME, image.getForecastReferenceTime());
        repr.withProperty(Constants.PHENOMENON, image.getPhenomenon());
        repr.withProperty(Constants.DATA_DIMENSIONS, image.getDataDimensions());
        repr.withProperty(Constants.FORECAST_TIME, image.getForecastTime());
        repr.withProperty(Constants.GEOGRAPHIC_REGION, image.getGeographicRegion());
        repr.withProperty(Constants.PROCESSING_PROFILE, image.getProcessingProfile());
        return repr;
    }

    public Representation getVideoAsRepresentation(URI self, Video video) {
        String selfHref = self + "/" + video.getId();
        Representation repr = this.newRepresentation(selfHref);
        repr.withLink(Constants.DATA, selfHref + "/data");
        repr.withProperty(Constants.RESOLUTION, video.getResolution());
        repr.withProperty(Constants.MODEL, video.getModel());
        repr.withProperty(Constants.FORECAST_REFERENCE_TIME, video.getForecastReferenceTime());
        repr.withProperty(Constants.PHENOMENON, video.getPhenomenon());
        repr.withProperty(Constants.DATA_DIMENSIONS, video.getDataDimensions());
        repr.withProperty(Constants.GEOGRAPHIC_REGION, video.getGeographicRegion());
        repr.withProperty(Constants.PROCESSING_PROFILE, video.getProcessingProfile());
        return repr;
    }
}
