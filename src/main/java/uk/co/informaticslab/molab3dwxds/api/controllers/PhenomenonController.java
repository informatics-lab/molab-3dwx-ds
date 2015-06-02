package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.services.MediaService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by tom on 02/06/2015.
 */
@Path(ModelsController.MODELS + "/{" + ModelController.MODEL + "}/{" + ForecastReferenceTimeController.FORECAST_REFERENCE_TIME + "}/{" + PhenomenonController.PHENOMENON + "}")
public class PhenomenonController extends BaseHalController {

    public static final String PHENOMENON = "phenomenon";

    private final MediaService mediaService;
    private final String model;
    private final DateTime forecastReferenceTime;
    private final String phenomenon;

    @Autowired
    public PhenomenonController(MyRepresentationFactory representationFactory,
                                UriResolver uriResolver,
                                MediaService mediaService,
                                @PathParam(ModelController.MODEL) String model,
                                @PathParam(ForecastReferenceTimeController.FORECAST_REFERENCE_TIME) String forecastReferenceTime,
                                @PathParam(PHENOMENON) String phenomenon) {
        super(representationFactory, uriResolver);
        this.mediaService = mediaService;
        this.model = model;
        this.forecastReferenceTime = new DateTime(forecastReferenceTime);
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
        if (mediaService.countImages(model, forecastReferenceTime, phenomenon) > 0) {
            repr.withLink("images", getSelf() + "/images");
        }
        if (mediaService.countVideos(model, forecastReferenceTime, phenomenon) > 0) {
            repr.withLink("videos", getSelf() + "/videos");
        }
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(ModelsController.MODELS, model, forecastReferenceTime, phenomenon);
    }

}

