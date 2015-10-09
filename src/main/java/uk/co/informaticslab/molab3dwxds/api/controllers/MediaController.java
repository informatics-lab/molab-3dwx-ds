package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.caching.utils.CacheControlUtils;
import uk.co.informaticslab.molab3dwxds.api.params.ByteRange;
import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.streaming.MediaStreamingResponse;
import uk.co.informaticslab.molab3dwxds.api.utils.MediaForm;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.domain.*;
import uk.co.informaticslab.molab3dwxds.services.MediaService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Controller for media
 */
@Path(MediaController.MEDIA)
public class MediaController extends BaseHalController {

    public static final String MEDIA = "media";

    private static final Logger LOG = LoggerFactory.getLogger(MediaController.class);
    public static final int MEGA_BYTES_15 = 1024 * 1024 * 15;

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
        MediaForm mediaForm = new MediaForm(form);
        String mimeType = mediaForm.getAsString(Constants.MIME_TYPE);

        if (mimeType.toLowerCase().contains("video")) {
            return insertAsVideo(mediaForm);
        } else if (mimeType.toLowerCase().contains("image")) {
            return insertAsImage(mediaForm);
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

            if (range == null && media instanceof Image) {
                return Response.ok(media.getData(), media.getMimeType())
                        .cacheControl(CacheControlUtils.permanent())
                        .build();
            } else if (range == null) {
                /*
                 * Always return a media streaming response for a video else we see an error in the logs.
                 */
                range = ByteRange.from(0);
            }

            return MediaStreamingResponse.media(media)
                    .range(range)
                    .cacheControl(CacheControlUtils.permanent())
                    .build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Path("/{" + Constants.ID + "}")
    @DELETE
    public Response deleteById(@PathParam(Constants.ID) String id) {
        mediaService.deleteById(id);
        return Response.noContent().build();
    }

    @Path("/all")
    @DELETE
    public Response deleteAll() {
        mediaService.deleteAll();
        return Response.noContent().build();
    }

    @Override
    public Representation getCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        repr.withLink("get_by_id", getSelf() + "{/" + Constants.ID + "}");
        repr.withLink("insert", getSelf());
        repr.withLink("delete_by_id", getSelf() + "{/" + Constants.ID + "}");
        repr.withLink("delete_all", getSelf() + "/all");
        return repr;
    }

    @Override
    public URI getSelf() {
        return uriResolver.mkUriForClass(this.getClass());
    }

    private Response insertAsVideo(MediaForm form) {
        byte[] data = form.getAsData(Constants.DATA);
        String mimeType = form.getAsString(Constants.MIME_TYPE);
        Resolution resolution = form.getAsResolution(Constants.RESOLUTION_X, Constants.RESOLUTION_Y);
        String model = form.getAsString(Constants.MODEL);
        DateTime forecastReferenceTime = form.getAsDateTime(Constants.FORECAST_REFERENCE_TIME);
        String phenomenon = form.getAsString(Constants.PHENOMENON);
        DataDimensions dataDimensions = form.getAsDataDimensions(Constants.DATA_DIMENSION_X, Constants.DATA_DIMENSION_Y, Constants.DATA_DIMENSION_Z);
        List<GeographicPoint> geographicRegion = form.getAsGeographicRegion(Constants.GEOGRAPHIC_REGION);
        String processingProfile = form.getAsString(Constants.PROCESSING_PROFILE);

        Media video = new Video(data,
                mimeType,
                resolution,
                model,
                forecastReferenceTime,
                phenomenon,
                dataDimensions,
                geographicRegion,
                processingProfile);

        return insertMedia(video);
    }

    private Response insertAsImage(MediaForm form) {
        byte[] data = form.getAsData(Constants.DATA);
        String mimeType = form.getAsString(Constants.MIME_TYPE);
        Resolution resolution = form.getAsResolution(Constants.RESOLUTION_X, Constants.RESOLUTION_Y);
        String model = form.getAsString(Constants.MODEL);
        DateTime forecastReferenceTime = form.getAsDateTime(Constants.FORECAST_REFERENCE_TIME);
        String phenomenon = form.getAsString(Constants.PHENOMENON);
        DataDimensions dataDimensions = form.getAsDataDimensions(Constants.DATA_DIMENSION_X, Constants.DATA_DIMENSION_Y, Constants.DATA_DIMENSION_Z);
        List<GeographicPoint> geographicRegion = form.getAsGeographicRegion(Constants.GEOGRAPHIC_REGION);
        String processingProfile = form.getAsString(Constants.PROCESSING_PROFILE);
        DateTime forecastTime = form.getAsDateTime(Constants.FORECAST_TIME);

        Media image = new Image(data,
                mimeType,
                resolution,
                model,
                forecastReferenceTime,
                phenomenon,
                dataDimensions,
                geographicRegion,
                processingProfile,
                forecastTime);

        return insertMedia(image);
    }

    private Response insertMedia(Media media) {

        if (media.getLength() == 0) {
            throw new IllegalArgumentException("data property CANNOT be zero length");
        }

        if (media.getLength() > MEGA_BYTES_15) {
            throw new IllegalArgumentException("data property CANNOT be greater than 15 megabytes");
        }

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