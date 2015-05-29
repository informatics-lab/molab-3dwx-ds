package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.google.common.io.ByteStreams;
import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;
import org.apache.tika.Tika;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.informaticslab.molab3dwxds.api.caching.utils.CacheControlUtils;
import uk.co.informaticslab.molab3dwxds.api.params.ByteRange;
import uk.co.informaticslab.molab3dwxds.api.params.DTRange;
import uk.co.informaticslab.molab3dwxds.api.params.ModelRunDTRange;
import uk.co.informaticslab.molab3dwxds.api.streaming.MediaStreamingResponse;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;
import uk.co.informaticslab.molab3dwxds.domain.*;
import uk.co.informaticslab.molab3dwxds.services.VideoService;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.net.URI;

import static uk.co.informaticslab.molab3dwxds.domain.Video.*;

/**
 * Controller class for the videos endpoint
 */
@Path(VideosController.VIDEOS)
public class VideosController {

    private static final Logger LOG = LoggerFactory.getLogger(VideosController.class);
    private static final Tika TIKA = new Tika();
    public static final String VIDEOS = "videos";

    private final VideoService videoService;
    private final RepresentationFactory representationFactory;
    private final UriResolver uriResolver;

    @Autowired
    public VideosController(VideoService videoService, RepresentationFactory representationFactory, UriResolver uriResolver) {
        this.videoService = videoService;
        this.representationFactory = representationFactory;
        this.uriResolver = uriResolver;
    }

    /**
     * Inserts the video along with it's metadata into the repository
     *
     * @param form submitted form containing video data and metadata
     * @return Created response pointing to newly accessible video
     */
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response insertVideo(FormDataMultiPart form) {
        LOG.debug("Video posted to endpoint...");

        DateTime modelRunDT;
        Phenomenon phenomenon;
        byte[] data;
        String mimeType;

        try {
            modelRunDT = new DateTime(form.getField(MODEL_RUN_DT).getValue());
            phenomenon = Phenomenon.valueOf(form.getField(PHENOMENON).getValue().toLowerCase());
            FormDataBodyPart bodyPart = form.getField(DATA);
            data = ByteStreams.toByteArray(bodyPart.getValueAs(InputStream.class));
            mimeType = TIKA.detect(data);
        } catch (Exception e) {
            LOG.error("Error reading multipart form data", e);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }

        Video video = new Video(data, mimeType, Resolution.asDefault(), modelRunDT, phenomenon, DataDimensions.asDefault());

        Video respVideo = videoService.insert(video);
        LOG.debug("Video inserted successfully...");
        return Response.created(URI.create(getSelf() + "/" + respVideo.getId())).build();
    }

    /**
     * Gets the video metadata
     *
     * @param id the unique id of the video
     * @return OK response with HAL video representation
     */
    @Path("/{" + ID + "}")
    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getVideoById(@PathParam(ID) String id) {
        Video video = videoService.getById(id, false);
        Representation repr = getVideoAsRepresentation(video);
        return Response.ok(repr).build();
    }

    /**
     * Gets the actual video data and renders it in the browser as an ogv
     *
     * @param id the unique id of the video
     * @return OK response with video data as the entity body
     */
    @Path("/{" + ID + "}/data")
    @GET
    public Response getVideoDataById(@PathParam(ID) String id,
                                     @HeaderParam("Range") ByteRange range) {
        Response response = null;
        Media media = videoService.getById(id, true);

        if (range == null) {
            range = ByteRange.from(0);
        }

        response = MediaStreamingResponse.media(media)
                .range(range)
                .cacheControl(CacheControlUtils.permanent())
                .build();

        return response;
    }


    /*
     * Disabled for now until proper user auth is enabled.
     */
//    @Path("/{" + ID + "}")
//    @DELETE
//    public Response deleteVideoById(@PathParam(ID) String id) {
//        videoService.delete(id);
//        return Response.noContent().build();
//    }

    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getVideosByFilter(@Context HttpServletRequest request,
                                      @QueryParam(PHENOMENON) Phenomenon phenomenon,
                                      @BeanParam ModelRunDTRange modelRunDTRange) {

        if (phenomenon == null && !modelRunDTRange.isDateRangeSet()) {
            return Response.ok(getResourceCapabilities()).build();
        }

        LOG.debug("{} = {}", PHENOMENON, phenomenon);
        LOG.debug("{} = {}", MODEL_RUN_DT, modelRunDTRange);

        Iterable<Video> videos = videoService.getByFilter(phenomenon, modelRunDTRange);

        Representation repr = representationFactory.newRepresentation(request.getRequestURI() + "?" + request.getQueryString());
        for (Video video : videos) {
            repr.withRepresentation(VIDEOS, getVideoAsRepresentation(video));
        }
        return Response.ok(repr).build();
    }

    @Path("/latest")
    @GET
    @Produces(RepresentationFactory.HAL_JSON)
    public Response getLatest(@QueryParam(Video.PHENOMENON) Phenomenon phenomenon) {
        Video video;
        if (phenomenon == null) {
            video = videoService.getLatest();
        } else {
            video = videoService.getLatestByPhenomenon(phenomenon);
        }
        Representation repr = representationFactory.newRepresentation(getSelf() + "/latest");
        repr.withRepresentation("latest", getVideoAsRepresentation(video));
        return Response.ok(repr).build();
    }


    public Representation getResourceCapabilities() {
        Representation repr = representationFactory.newRepresentation(getSelf());
        repr.withLink("save_video", getSelf());
        repr.withLink("get_video_by_id", getSelf() + "{/" + ID + "}");
        repr.withLink("get_latest_video", getSelf() + "/latest{?" + PHENOMENON + "}");
        repr.withLink("get_videos_by_filter", buildFilterURI());
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
                .append("}")
                .toString();
    }

    private Representation getVideoAsRepresentation(Video video) {
        String selfHref = getSelf() + "/" + video.getId();
        Representation repr = representationFactory.newRepresentation(selfHref);
        repr.withLink(Video.DATA, selfHref + "/data");
        repr.withProperty(Video.MODEL_RUN_DT, video.getModelRunDT());
        repr.withProperty(Video.PHENOMENON, video.getPhenomenon());
        return repr;
    }

    public URI getSelf() {
        return uriResolver.mkUriForClass(VideosController.class);
    }

}
