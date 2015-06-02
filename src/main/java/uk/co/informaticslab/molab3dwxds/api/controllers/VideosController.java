package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.domain.Constants;
import uk.co.informaticslab.molab3dwxds.domain.Video;
import uk.co.informaticslab.molab3dwxds.services.MediaService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Controller class for the videos endpoint
 */
@Path(ModelsController.MODELS + "/{" + ModelController.MODEL + "}/{" + ForecastReferenceTimeController.FORECAST_REFERENCE_TIME + "}/" + VideosController.VIDEOS)
public class VideosController extends BaseHalController {

    public static final String VIDEOS = "videos";

    private static final Logger LOG = LoggerFactory.getLogger(VideosController.class);

    private final MediaService mediaService;
    private final String model;
    private final DateTime forecastReferenceTime;

    @Autowired
    public VideosController(MyRepresentationFactory representationFactory,
                            UriResolver uriResolver,
                            MediaService mediaService,
                            @PathParam(ModelController.MODEL) String model,
                            @PathParam(ForecastReferenceTimeController.FORECAST_REFERENCE_TIME) String forecastReferenceTime) {
        super(representationFactory, uriResolver);
        this.mediaService = mediaService;
        this.model = model;
        this.forecastReferenceTime = new DateTime(forecastReferenceTime);
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getVideosByFilter(@Context HttpServletRequest request,
                                      @QueryParam(Constants.PHENOMENON) String phenomenon) {

        if (phenomenon == null) {
            return Response.ok(getCapabilities()).build();
        }

        LOG.debug("{} = {}", Constants.PHENOMENON, phenomenon);

        Iterable<Video> videos = mediaService.getVideosByFilter(phenomenon);

        Representation repr = representationFactory.newRepresentation(request.getRequestURI() + "?" + request.getQueryString());
        for (Video video : videos) {
            repr.withRepresentation(VIDEOS, representationFactory.getVideoAsRepresentation(uriResolver.mkUri(MediaController.MEDIA), video));
        }
        return Response.ok(repr).build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        repr.withLink("get_videos_by_filter", getSelf() + "{?" + Constants.PHENOMENON + "}");
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(ModelsController.MODELS, model, forecastReferenceTime, VIDEOS);
    }
}
