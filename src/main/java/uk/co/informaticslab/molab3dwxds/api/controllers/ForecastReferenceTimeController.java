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
 * Created by tom on 29/05/2015.
 */
@Path(ModelsController.MODELS + "/{" + ModelController.MODEL + "}/{" + ForecastReferenceTimeController.FORECAST_REFERENCE_TIME + "}")
public class ForecastReferenceTimeController extends BaseHalController {

    public static final String FORECAST_REFERENCE_TIME = "forecast_reference_time";

    private final MediaService mediaService;
    private final String model;
    private final DateTime forecastReferenceTime;

    @Autowired
    public ForecastReferenceTimeController(MyRepresentationFactory representationFactory,
                                           UriResolver uriResolver,
                                           MediaService mediaService,
                                           @PathParam(ModelController.MODEL) String model,
                                           @PathParam(FORECAST_REFERENCE_TIME) String forecastReferenceTime) {
        super(representationFactory, uriResolver);
        this.mediaService = mediaService;
        this.model = model;
        this.forecastReferenceTime = new DateTime(forecastReferenceTime);
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response get() {
        System.out.println("getting frt resource");
        return Response.ok(getCapabilities()).build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        if (mediaService.countImages(model, forecastReferenceTime) > 0) {
            repr.withLink("images", getSelf() + "/images");
        }
        if (mediaService.countVideos(model, forecastReferenceTime) > 0) {
            repr.withLink("videos", getSelf() + "/videos");
        }
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(ModelsController.MODELS, model, forecastReferenceTime);
    }

}