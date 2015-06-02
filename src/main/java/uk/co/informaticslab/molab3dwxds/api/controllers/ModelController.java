package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
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
        repr.withLink("get_by_forecast_reference_time", getSelf() + "{/" + FORECAST_REFERENCE_TIME + "}");
        repr.withProperty(FORECAST_REFERENCE_TIME + "s", mediaService.getForecastReferenceTimes(model));
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(ModelsController.MODELS, model);
    }

}
