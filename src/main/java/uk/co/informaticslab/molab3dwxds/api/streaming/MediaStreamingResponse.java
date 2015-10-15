package uk.co.informaticslab.molab3dwxds.api.streaming;

import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.api.params.ByteRange;
import uk.co.informaticslab.molab3dwxds.domain.Media;

import javax.ws.rs.core.*;

/**
 * Created by tom on 20/05/2015.
 */
public final class MediaStreamingResponse {

    private MediaStreamingResponse() {
    }

    public static MediaStreamingResponseBuilder media(Media media) {
        return new MediaStreamingResponseBuilder().media(media);
    }

    public static final class MediaStreamingResponseBuilder {

        public static int DEFAULT_CHUNK_SIZE = 1000 * 1000; // 1MB

        private final Response.ResponseBuilder respBuilder = Response.status(Response.Status.PARTIAL_CONTENT);

        private Media media;
        private ByteRange byteRange;
        private int chunkSize = DEFAULT_CHUNK_SIZE;

        public MediaStreamingResponseBuilder media(Media media) {
            this.media = media;
            return this;
        }

        public MediaStreamingResponseBuilder range(ByteRange byteRange) {
            this.byteRange = byteRange;
            return this;
        }

        public MediaStreamingResponseBuilder chunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
            return this;
        }

        public MediaStreamingResponseBuilder cacheControl(CacheControl cacheControl) {
            this.respBuilder.cacheControl(cacheControl);
            return this;
        }

        public MediaStreamingResponseBuilder lastModified(DateTime lastModified) {
            this.respBuilder.lastModified(lastModified.toDate());
            return this;
        }

        public MediaStreamingResponseBuilder tag(EntityTag tag) {
            this.respBuilder.tag(tag);
            return this;
        }

        public Response build() {
            ByteRange byteRange = this.byteRange.calculateFor(this.chunkSize, this.media.getLength());

            StreamingOutput streamer = new MediaStreamer(this.media, byteRange);

            return this.respBuilder
                    .type(this.media.getMimeType())
                    .header("Accept-Ranges", "bytes")
                    .header("Content-Range", toContentRange(byteRange, this.media))
                    .header(HttpHeaders.CONTENT_LENGTH, byteRange.getLength())
                    .entity(streamer).build();
        }

        private String toContentRange(ByteRange byteRange, Media media) {
            // bytes %d-%d/%d
            StringBuilder sb = new StringBuilder("bytes ");
            sb.append(byteRange.getFrom()).append("-").append(byteRange.getTo()).append("/").append(media.getLength());
            return sb.toString();
        }
    }
}