package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.UriResolver;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Created by tom on 15/05/2015.
 */
@Path("/")
public class RootController {

    private final RepresentationFactory factory;
    private final UriResolver uriResolver;

    @Autowired
    public RootController(RepresentationFactory factory, UriResolver uriResolver) {
        this.factory = factory;
        this.uriResolver = uriResolver;
    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getCapabilities() {
        Representation repr = factory.newRepresentation(uriResolver.getBaseUriString());
        repr.withLink(ImagesController.IMAGES, uriResolver.mkUriForClass(ImagesController.class));
        repr.withLink(VideosController.VIDEOS, uriResolver.mkUriForClass(VideosController.class));
        return Response.ok(repr).build();
    }

}
