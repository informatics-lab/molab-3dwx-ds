package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.domain.Video;
import uk.co.informaticslab.molab3dwxds.services.MediaService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Controller class for the videos endpoint
 */
@Path(ModelsController.MODELS + "/{" + ModelController.MODEL + "}/{" + ForecastReferenceTimeController.FORECAST_REFERENCE_TIME + "}/{" + PhenomenonController.PHENOMENON + "}/" + VideosController.VIDEOS)
public class VideosController extends BaseHalController {

    public static final String VIDEOS = "videos";

    private static final Logger LOG = LoggerFactory.getLogger(VideosController.class);

    private final MediaService mediaService;
    private final String model;
    private final DateTime forecastReferenceTime;
    private final String phenomenon;

    @Autowired
    public VideosController(MyRepresentationFactory representationFactory,
                            UriResolver uriResolver,
                            MediaService mediaService,
                            @PathParam(ModelController.MODEL) String model,
                            @PathParam(ForecastReferenceTimeController.FORECAST_REFERENCE_TIME) DateTime forecastReferenceTime,
                            @PathParam(PhenomenonController.PHENOMENON) String phenomenon) {
        super(representationFactory, uriResolver);
        this.mediaService = mediaService;
        this.model = model;
        this.forecastReferenceTime = forecastReferenceTime;
        this.phenomenon = phenomenon;
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getVideosByFilter() {

        Iterable<Video> videos = mediaService.getVideosByFilter(model, forecastReferenceTime, phenomenon);

        Representation repr = getCapabilities();
        for (Video video : videos) {
            repr.withRepresentation(VIDEOS, representationFactory.getVideoAsRepresentation(uriResolver.mkUri(MediaController.MEDIA), video));
        }
        return Response.ok(repr).build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(ModelsController.MODELS, model, forecastReferenceTime, VIDEOS);
    }
}
