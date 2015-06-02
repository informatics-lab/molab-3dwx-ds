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
import uk.co.informaticslab.molab3dwxds.api.caching.utils.CacheControlUtils;
import uk.co.informaticslab.molab3dwxds.api.params.ByteRange;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.streaming.MediaStreamingResponse;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.domain.*;
import uk.co.informaticslab.molab3dwxds.services.MediaService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

/**
 * Controller for media
 */
@Path(MediaController.MEDIA)
public class MediaController extends BaseHalController {

    public static final String MEDIA = "media";

    private static final Logger LOG = LoggerFactory.getLogger(MediaController.class);

    private final MediaService mediaService;

    @Autowired
    public MediaController(MyRepresentationFactory representationFactory,
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

    /**
     * Inserts a media entity along with it's metadata into the repository
     *
     * @param form submitted form containing media data and metadata
     * @return Created response pointing to newly accessible media
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response insert(FormDataMultiPart form) {
        LOG.debug("Media posted to endpoint...");

        String mimeType = form.getField(Constants.MIME_TYPE).getValue();

        if (mimeType.toLowerCase().contains("video")) {
            return insertAsVideo(form);
        } else if (mimeType.toLowerCase().contains("image")) {
            return insertAsImage(form);
        } else {
            return Response.status(Response.Status.BAD_REQUEST).entity("mime type must be that of either image or video").build();
        }
    }

    @Path("/{" + Constants.ID + "}")
    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getById(@PathParam(Constants.ID) String id) {

        Optional<? extends Media> optional = mediaService.getById(id, false);
        if (optional.isPresent()) {
            Media media = optional.get();
            if (media instanceof Image) {
                Image image = (Image) media;
                return Response.ok(representationFactory.getImageAsRepresentation(getSelf(), image)).build();
            } else if (media instanceof Video) {
                Video video = (Video) media;
                return Response.ok(representationFactory.getVideoAsRepresentation(getSelf(), video)).build();
            } else {
                return Response.serverError().build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Gets the actual data and renders it in the browser.
     *
     * @param id the unique id of the media
     * @return OK response with media data as the entity body
     */
    @Path("/{" + Constants.ID + "}/data")
    @GET
    public Response getDataById(@PathParam(Constants.ID) String id,
                                @HeaderParam("Range") ByteRange range) {

        Optional<? extends Media> optional = mediaService.getById(id, true);
        if (optional.isPresent()) {
            Media media = optional.get();

            if (range == null) {
                return Response.ok(media.getData(), media.getMimeType())
                        .cacheControl(CacheControlUtils.permanent())
                        .build();
            } else {
                return MediaStreamingResponse.media(media)
                        .range(range)
                        .cacheControl(CacheControlUtils.permanent())
                        .build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /*
     * Disabled for now until proper user auth is enabled.
     */
//    @Path("/{" + ID + "}")
//    @DELETE
//    public Response deleteById(@PathParam(ID) String id) {
//        mediaService.delete(id);
//        return Response.noContent().build();
//    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        repr.withLink("get_by_id", getSelf() + "{/" + Constants.ID + "}");
        repr.withLink("insert", getSelf());
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUriForClass(this.getClass());
    }

    private Response insertAsVideo(FormDataMultiPart form) {
        byte[] data;
        String mimeType;
        Resolution resolution;
        String model;
        DateTime forecastReferenceTime;
        String phenomenon;
        DataDimensions dataDimensions;

        try {

            FormDataBodyPart bodyPart = form.getField(Constants.DATA);
            data = ByteStreams.toByteArray(bodyPart.getValueAs(InputStream.class));
            mimeType = form.getField(Constants.MIME_TYPE).getValue();
            resolution = Resolution.asDefault();
            model = form.getField(Constants.MODEL).getValue();
            forecastReferenceTime = new DateTime(form.getField(Constants.FORECAST_REFERENCE_TIME).getValue());
            phenomenon = form.getField(Constants.PHENOMENON).getValue();
            dataDimensions = DataDimensions.asDefault();

        } catch (Exception e) {
            LOG.error("Error reading multipart form data", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        Media video = new Video(data,
                mimeType,
                resolution,
                model,
                forecastReferenceTime,
                phenomenon,
                dataDimensions);

        return insertMedia(video);
    }

    private Response insertAsImage(FormDataMultiPart form) {
        byte[] data;
        String mimeType;
        Resolution resolution;
        String model;
        DateTime forecastReferenceTime;
        String phenomenon;
        DataDimensions dataDimensions;
        DateTime forecastTime;

        try {

            FormDataBodyPart bodyPart = form.getField(Constants.DATA);
            data = ByteStreams.toByteArray(bodyPart.getValueAs(InputStream.class));
            mimeType = form.getField(Constants.MIME_TYPE).getValue();
            resolution = Resolution.asDefault();
            model = form.getField(Constants.MODEL).getValue();
            forecastReferenceTime = new DateTime(form.getField(Constants.FORECAST_REFERENCE_TIME).getValue());
            phenomenon = form.getField(Constants.PHENOMENON).getValue();
            dataDimensions = DataDimensions.asDefault();
            forecastTime = new DateTime(form.getField(Constants.FORECAST_TIME).getValue());

        } catch (Exception e) {
            LOG.error("Error reading multipart form data", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        Media image = new Image(data,
                mimeType,
                resolution,
                model,
                forecastReferenceTime,
                phenomenon,
                dataDimensions,
                forecastTime);

        return insertMedia(image);
    }

    private Response insertMedia(Media media) {
        Optional<? extends Media> optional = mediaService.insert(media);
        if (optional.isPresent()) {
            Media insertedMedia = optional.get();
            LOG.debug("Media inserted successfully...");
            return Response.created(URI.create(getSelf() + "/" + insertedMedia.getId())).build();
        } else {
            return Response.serverError().build();
        }
    }

}