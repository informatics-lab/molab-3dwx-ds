package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.domain.Image;
import uk.co.informaticslab.molab3dwxds.domain.Video;
import uk.co.informaticslab.molab3dwxds.services.impl.AdvancedMongoMediaService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 02/06/2015.
 */
@Path(ModelsController.MODELS + "/{" + ModelController.MODEL + "}/{" + ForecastReferenceTimeController.FORECAST_REFERENCE_TIME + "}/{" + PhenomenonController.PHENOMENON + "}")
public class PhenomenonController extends BaseHalController {

    public static final String PHENOMENON = "phenomenon";

    private final AdvancedMongoMediaService mediaService;
    private final String model;
    private final DateTime forecastReferenceTime;
    private final String phenomenon;

    @Autowired
    public PhenomenonController(MyRepresentationFactory representationFactory,
                                UriResolver uriResolver,
                                AdvancedMongoMediaService mediaService,
                                @PathParam(ModelController.MODEL) String model,
                                @PathParam(ForecastReferenceTimeController.FORECAST_REFERENCE_TIME) DateTime forecastReferenceTime,
                                @PathParam(PHENOMENON) String phenomenon) {
        super(representationFactory, uriResolver);
        this.mediaService = mediaService;
        this.model = model;
        this.forecastReferenceTime = forecastReferenceTime;
        this.phenomenon = phenomenon;
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response get() {
        return Response.ok(getCapabilities()).build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        for (String processingProfile : getProcessingProfilesFromMediaService(model, forecastReferenceTime, phenomenon)) {
            repr.withLink(processingProfile, getSelf() + "/" + processingProfile);
        }
        return repr;
    }

    private List<String> getProcessingProfilesFromMediaService(String model, DateTime forecastReferenceTime, String phenomenon) {
        final List<String> uniqueProcessingProfiles = new ArrayList<>();

        for (Image image : mediaService.findAllImageMetaByFilter(model, forecastReferenceTime, phenomenon, null, null)) {
            if (!uniqueProcessingProfiles.contains(image.getPhenomenon())) {
                uniqueProcessingProfiles.add(image.getPhenomenon());
            }
        }
        for (Video video : mediaService.findAllVideoMetaByFilter(model, forecastReferenceTime, phenomenon, null)) {
            if (!uniqueProcessingProfiles.contains(video.getPhenomenon())) {
                uniqueProcessingProfiles.add(video.getPhenomenon());
            }
        }
        return uniqueProcessingProfiles;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(ModelsController.MODELS, model, forecastReferenceTime, phenomenon);
    }

}

