package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.google.common.io.ByteStreams;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.UriResolver;
import uk.co.informaticslab.molab3dwxds.api.caching.utils.CacheControlUtils;
import uk.co.informaticslab.molab3dwxds.api.params.DTRange;
import uk.co.informaticslab.molab3dwxds.api.params.ForecastDTRange;
import uk.co.informaticslab.molab3dwxds.api.params.ModelRunDTRange;
import uk.co.informaticslab.molab3dwxds.domain.Image;
import uk.co.informaticslab.molab3dwxds.domain.Phenomenon;
import uk.co.informaticslab.molab3dwxds.services.ImageService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;

import static uk.co.informaticslab.molab3dwxds.domain.Image.*;

/**
 * Controller class for the images endpoint
 */
@Path(ImagesController.IMAGES)
public class ImagesController {

    private static final Logger LOG = LoggerFactory.getLogger(ImagesController.class);

    public static final String IMAGES = "images";

    private final ImageService imageService;
    private final RepresentationFactory representationFactory;
    private final UriResolver uriResolver;

    @Autowired
    public ImagesController(ImageService imageService, RepresentationFactory representationFactory, UriResolver uriResolver) {
        this.imageService = imageService;
        this.representationFactory = representationFactory;
        this.uriResolver = uriResolver;
    }

    /**
     * Inserts the image along with it's metadata into the repository
     *
     * @param form submitted form containing image data and metadata
     * @return Created response pointing to newly accessible image
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response insertImage(FormDataMultiPart form) {
        LOG.debug("Image posted to endpoint...");

        DateTime modelRunDT;
        DateTime forecastDT;
        Phenomenon phenomenon;
        byte[] data;

        try {
            modelRunDT = new DateTime(form.getField(MODEL_RUN_DT).getValue());
            forecastDT = new DateTime(form.getField(FORECAST_DT).getValue());
            phenomenon = Phenomenon.valueOf(form.getField(PHENOMENON).getValue().toLowerCase());
            FormDataBodyPart bodyPart = form.getField(DATA);
            data = ByteStreams.toByteArray(bodyPart.getValueAs(InputStream.class));
        } catch (Exception e) {
            LOG.error("Error reading multipart form data", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        Image image = new Image(modelRunDT, forecastDT, phenomenon, data);

        Image respImage = imageService.insert(image);
        LOG.debug("Image inserted successfully...");
        return Response.created(URI.create(getSelf() + "/" + respImage.getId())).build();

    }

    /**
     * Gets the image metadata
     *
     * @param id the unique id of the image
     * @return OK response with HAL image representation
     */
    @Path("/{" + ID + "}")
    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getImageById(@PathParam(ID) String id) {
        Image image = imageService.getById(id, false);
        Representation repr = getImageAsRepresentation(image);
        return Response.ok(repr).build();
    }

    /**
     * Gets the actual image data and renders it in the browser as a png
     *
     * @param id the unique id of the image
     * @return OK response with image data as the entity body
     */
    @Path("/{" + ID + "}/data")
    @GET
    @Produces("image/png")
    public Response getImageDataById(@PathParam(ID) String id) {
        byte[] data = imageService.getById(id, true).getData();
        return Response.ok(data).cacheControl(CacheControlUtils.permanent()).build();
    }


    /*
     * Disabled for now until proper user auth is enabled.
     */
//    @Path("/{" + ID + "}")
//    @DELETE
//    public Response deleteImageById(@PathParam(ID) String id) {
//        imageService.delete(id);
//        return Response.noContent().build();
//    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getImagesByFilter(@Context HttpServletRequest request,
                                      @QueryParam(PHENOMENON) Phenomenon phenomenon,
                                      @BeanParam ModelRunDTRange modelRunDTRange,
                                      @BeanParam ForecastDTRange forecastDTRange) {

        if (phenomenon == null && !modelRunDTRange.isDateRangeSet() && !forecastDTRange.isDateRangeSet()) {
            //return the default resource capabilities
            return Response.ok(getResourceCapabilities()).build();
        }

        LOG.debug("{} = {}", PHENOMENON, phenomenon);
        LOG.debug("{} = {}", MODEL_RUN_DT, modelRunDTRange);
        LOG.debug("{} = {}", FORECAST_DT, forecastDTRange);

        Iterable<Image> images = imageService.getByFilter(phenomenon, modelRunDTRange, forecastDTRange);

        Representation repr = representationFactory.newRepresentation(request.getRequestURI() + "?" + request.getQueryString());
        for (Image image : images) {
            repr.withRepresentation(IMAGES, getImageAsRepresentation(image));
        }
        return Response.ok(repr).build();
    }

    public Representation getResourceCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        repr.withLink("save_image", getSelf());
        repr.withLink("get_image_by_id", getSelf() + "{/" + ID + "}");
//        repr.withLink("delete-image-by-id", Application.BASE_URI + IMAGES + "{/" + ID + "}");
        repr.withLink("get_images_by_filter", buildFilterURI());
        return repr;
    }

    private String buildFilterURI() {
        return new StringBuilder()
                .append(getSelf())
                .append("{?")
                .append(PHENOMENON)
                .append("," + MODEL_RUN_DT + "_" + DTRange.GT)
                .append("," + MODEL_RUN_DT + "_" + DTRange.GTE)
                .append("," + MODEL_RUN_DT + "_" + DTRange.LT)
                .append("," + MODEL_RUN_DT + "_" + DTRange.LTE)
                .append("," + FORECAST_DT + "_" + DTRange.GT)
                .append("," + FORECAST_DT + "_" + DTRange.GTE)
                .append("," + FORECAST_DT + "_" + DTRange.LT)
                .append("," + FORECAST_DT + "_" + DTRange.LTE)
                .append("}")
                .toString();
    }

    private Representation getImageAsRepresentation(Image image) {
        String selfHref = getSelf() + "/" + image.getId();
        Representation repr = representationFactory.newRepresentation(selfHref);
        repr.withLink(Image.DATA, selfHref + "/data");
        repr.withProperty(Image.MODEL_RUN_DT, image.getModelRunDT());
        repr.withProperty(Image.FORECAST_DT, image.getForecastDT());
        repr.withProperty(Image.PHENOMENON, image.getPhenomenon());
        return repr;
    }

    public URI getSelf() {
        return uriResolver.mkUriForClass(ImagesController.class);
    }

}
