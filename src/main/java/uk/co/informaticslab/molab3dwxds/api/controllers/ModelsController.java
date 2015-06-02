package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.services.MediaService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Created by tom on 29/05/2015.
 */
@Path(ModelsController.MODELS)
public class ModelsController extends BaseHalController {

    public static final String MODELS = "models";

    private final MediaService mediaService;

    @Autowired
    public ModelsController(MyRepresentationFactory representationFactory,
                            UriResolver uriResolver,
                            MediaService mediaService) {
        super(representationFactory, uriResolver);
        this.mediaService = mediaService;
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response get() {
        return Response.ok(getCapabilities()).build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        for (String model : mediaService.getModels()) {
            repr.withLink(model, getSelf() + "/" + model);
        }
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri(MODELS);
    }

}