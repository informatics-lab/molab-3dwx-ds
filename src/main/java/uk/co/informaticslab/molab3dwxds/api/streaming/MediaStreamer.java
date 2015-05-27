package uk.co.informaticslab.molab3dwxds.api.streaming;

import uk.co.informaticslab.molab3dwxds.api.params.ByteRange;
import uk.co.informaticslab.molab3dwxds.domain.Media;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by tom on 20/05/2015.
 */
public class MediaStreamer implements StreamingOutput {

    private final Media media;
    private final ByteRange byteRange;

    public MediaStreamer(Media media, ByteRange byteRange) {
        this.media = media;
        this.byteRange = byteRange;
    }

    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException {

        byte[] data = media.getData();
        int offset = byteRange.getFrom();
        int length = byteRange.getLength();

        output.write(data, offset, length);
        output.flush();
    }
}
