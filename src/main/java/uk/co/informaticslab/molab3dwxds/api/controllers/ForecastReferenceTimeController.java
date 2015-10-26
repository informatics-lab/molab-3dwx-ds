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
 * Created by tom on 29/05/2015.
 */
@Path(ModelsController.MODELS + "/{" + ModelController.MODEL + "}/{" + ForecastReferenceTimeController.FORECAST_REFERENCE_TIME + "}")
public class ForecastReferenceTimeController extends BaseHalController {

    public static final String FORECAST_REFERENCE_TIME = "forecast_reference_time";

    private final AdvancedMongoMediaService mediaService;
    private final String model;
    private final DateTime forecastReferenceTime;

    @Autowired
    public ForecastReferenceTimeController(MyRepresentationFactory representationFactory,
                                           UriResolver uriResolver,
                                           AdvancedMongoMediaService mediaService,
                                           @PathParam(ModelController.MODEL) String model,
                                           @PathParam(FORECAST_REFERENCE_TIME) DateTime forecastReferenceTime) {
        super(representationFactory, uriResolver);
        this.mediaService = mediaService;
        this.model = model;
        this.forecastReferenceTime = forecastReferenceTime;
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response get() {
        return Response.ok(getCapabilities()).build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());

        for (String phenomenon : getPhenomenonsFromMediaService(model, forecastReferenceTime)) {
            repr.withLink(phenomenon, getSelf() + "/" + phenomenon);
        }

        return repr;
    }

    private List<String> getPhenomenonsFromMediaService(String model, DateTime forecastReferenceTime) {
        final List<String> uniquePhenomenons = new ArrayList<>();

        for (Image image : mediaService.findAllImageMetaByFilter(model, forecastReferenceTime, null, null, null)) {
            if (!uniquePhenomenons.contains(image.getPhenomenon())) {
                uniquePhenomenons.add(image.getPhenomenon());
            }
        }
        for (Video video : mediaService.findAllVideoMetaByFilter(model, forecastReferenceTime, null, null)) {
            if (!uniquePhenomenons.contains(video.getPhenomenon())) {
                uniquePhenomenons.add(video.getPhenomenon());
            }
        }
        return uniquePhenomenons;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(ModelsController.MODELS, model, forecastReferenceTime);
    }

}