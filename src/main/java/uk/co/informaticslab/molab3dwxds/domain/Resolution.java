package uk.co.informaticslab.molab3dwxds.domain;

/**
 * Defines the media resolution
 */
public class Resolution {

    private static int DEFAULT_X = 4096;
    private static int DEFAULT_Y = 4096;

    private final int x;
    private final int y;

    public Resolution(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static Resolution asDefault() {
        return new Resolution(DEFAULT_X, DEFAULT_Y);
    }

    @Override
    public String toString() {
        return "Resolution{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
