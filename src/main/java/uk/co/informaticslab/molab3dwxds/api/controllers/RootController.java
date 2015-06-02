package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.URI;

/**
 * Root controller for the API
 */
@Path("/")
public class RootController extends BaseHalController {

    @Autowired
    public RootController(MyRepresentationFactory factory, UriResolver uriResolver) {
        super(factory, uriResolver);
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response get() {
        return Response.ok(getCapabilities()).build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(uriResolver.getBaseUriString());
        repr.withLink(ModelsController.MODELS, uriResolver.mkUriForClass(ModelsController.class));
        repr.withLink(MediaController.MEDIA, uriResolver.mkUriForClass(MediaController.class));
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUri();
    }

}
