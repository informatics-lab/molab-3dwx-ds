package uk.co.informaticslab.molab3dwxds.api.params;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ByteRange {

    public static final int UNSPECIFIED = Integer.MIN_VALUE;

    private static final Pattern bytesRangePattern = Pattern.compile("bytes=(\\d*)-(\\d*)");

    public static ByteRange valueOf(String value) {

        Matcher m = bytesRangePattern.matcher(value);
        if (m.matches()) {

            int from = parseBytePos(m.group(1));
            int to = parseBytePos(m.group(2));

            return new ByteRange(from, to);
        }
        throw new IllegalArgumentException(String.format("Expected value of format: %s, Got: %s", bytesRangePattern, value));
    }

    public static ByteRange between(int from, int to) {
        return new ByteRange(from, to);
    }

    public static ByteRange from(int from) {
        return new ByteRange(from, UNSPECIFIED);
    }

    public static ByteRange firstBytes(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("num must be greater than 0, Got: " + num);
        }
        return new ByteRange(0, num - 1);
    }

    public static ByteRange lastBytes(int num) {
        if (num <= 0) {
            throw new IllegalArgumentException("num must be greater than 0, Got: " + num);
        }
        return new ByteRange(UNSPECIFIED, num);
    }

    private static int parseBytePos(String value) {
        int pos = UNSPECIFIED;
        if (!value.isEmpty()) {
            pos = Integer.parseInt(value);
        }
        return pos;
    }

    private final int from;
    private final int to;
    private final int length;

    private ByteRange(final int from, final int to) {
        if (from < 0 && from != UNSPECIFIED) {
            throw new IllegalArgumentException("from must be a positive integer: " + from);
        }
        if (to < 0 && to != UNSPECIFIED) {
            throw new IllegalArgumentException("to must be a positive integer: " + to);
        }

        this.from = from;
        this.to = to;

        if (from == UNSPECIFIED || to == UNSPECIFIED) {
            this.length = UNSPECIFIED;
        } else if (from > to) {
            throw new IllegalArgumentException(String.format("from cannot be greater than to, Got from=%d, to=%d", from, to));
        } else {
            this.length = to - from + 1;
        }
    }

    public int getFrom() {
        return from;
    }

    public boolean hasFrom() {
        return from != UNSPECIFIED;
    }

    public int getTo() {
        return to;
    }

    public boolean hasTo() {
        return to != UNSPECIFIED;
    }

    public int getLength() {
        return length;
    }

    public boolean hasLength() {
        return length != UNSPECIFIED;
    }

    public ByteRange calculateFor(int chunkSize, int contentLength) {

        int newFrom;
        int newTo;
        if (hasLength() && getLength() <= chunkSize) {
            newFrom = getFrom();
            newTo = getTo();
        } else {
            newFrom = calculateFrom(contentLength);
            newTo = calculateTo(newFrom, chunkSize, contentLength);
        }
        return new ByteRange(newFrom, newTo);
    }

    private int calculateFrom(int contentLength) {

        int from;
        if (this.from == UNSPECIFIED) {
            from = contentLength - this.to;
        } else {
            from = this.from;
        }
        return from;
    }

    private int calculateTo(int from, int chunkSize, int contentLength) {

        int to = from + chunkSize - 1;
        if (to >= contentLength) {
            to = contentLength - 1;
        }
        return to;
    }
}
