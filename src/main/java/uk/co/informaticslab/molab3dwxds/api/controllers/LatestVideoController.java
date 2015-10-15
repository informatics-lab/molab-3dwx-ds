package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.domain.Video;
import uk.co.informaticslab.molab3dwxds.services.impl.MongoAggregationMediaService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;


/**
 * Controller class for the latest videos endpoint
 */
@Path(MediaController.MEDIA + "/" + LatestVideoController.VIDEOS)
public class LatestVideoController extends BaseHalController {

    public static final String VIDEOS = "videos";

    private final MongoAggregationMediaService mongoAggregationMediaService;

    @Autowired
    public LatestVideoController(MyRepresentationFactory representationFactory,
                                 UriResolver uriResolver,
                                 MongoAggregationMediaService mongoAggregationMediaService) {
        super(representationFactory, uriResolver);
        this.mongoAggregationMediaService = mongoAggregationMediaService;
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response get() {
        return Response.ok(getCapabilities()).build();
    }

    @Path("/latest")
    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getLatestVideo() {

        Representation repr = representationFactory.newRepresentation(getSelf() + "/latest");
        for (Video video : mongoAggregationMediaService.getLatestVideosGroupedByPhenomenon()) {
            repr.withRepresentation("latest_videos", representationFactory.getVideoAsRepresentation(uriResolver.mkUri(MediaController.MEDIA), video));
        }
        return Response.ok(repr).build();

    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        repr.withLink("latest", getSelf() + "/latest");
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(MediaController.MEDIA, VIDEOS);
    }
}
