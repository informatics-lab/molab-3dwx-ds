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
import java.util.List;
import java.util.Optional;

import static uk.co.informaticslab.molab3dwxds.domain.Constants.FORECAST_REFERENCE_TIME;

/**
 * Created by tom on 29/05/2015.
 */
@Path(ModelsController.MODELS + "/{" + ModelController.MODEL + "}")
public class ModelController extends BaseHalController {

    public static final String MODEL = "model";

    private final MediaService mediaService;
    private final String model;

    @Autowired
    public ModelController(MyRepresentationFactory representationFactory,
                           UriResolver uriResolver,
                           MediaService mediaService,
                           @PathParam(MODEL) String model) {
        super(representationFactory, uriResolver);
        this.mediaService = mediaService;
        this.model = model;
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response get() {
        return Response.ok(getCapabilities()).build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());

        Optional<DateTime> optional = mediaService.getLatestForecastReferenceTime(model);
        if (optional.isPresent()) {
            DateTime latest = optional.get();
            repr.withLink("latest", uriResolver.appendToSelf(getSelf(), latest));
        }
        List<DateTime> forecastReferenceTimes = mediaService.getForecastReferenceTimes(model);
        if (!forecastReferenceTimes.isEmpty()) {
            repr.withLink("get_by_forecast_reference_time", getSelf() + "{/" + FORECAST_REFERENCE_TIME + "}");
            repr.withProperty(FORECAST_REFERENCE_TIME + "s", forecastReferenceTimes);
        }
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(ModelsController.MODELS, model);
    }

}
