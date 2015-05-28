package uk.co.informaticslab.molab3dwxds.domain;

/**
 * Defines the initial dimensions of the data.
 */
public class DataDimensions {

    private static final int DEFAULT_X = 623;
    private static final int DEFAULT_Y = 812;
    private static final int DEFAULT_Z = 70;

    private final int x;
    private final int y;
    private final int z;

    public DataDimensions(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public static DataDimensions asDefault() {
        return new DataDimensions(DEFAULT_X, DEFAULT_Y, DEFAULT_Z);
    }

    @Override
    public String toString() {
        return "DataDimensions{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
