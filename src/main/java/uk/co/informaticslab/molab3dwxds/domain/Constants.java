package uk.co.informaticslab.molab3dwxds.domain;

/**
 * Created by tom on 29/05/2015.
 */
public final class Constants {

    private Constants() {

    }

    public static final String ID = "id";

    public static final String DATA = "data";
    public static final String MIME_TYPE = "mime_type";
    public static final String RESOLUTION = "resolution";

    public static final String RESOLUTION_X = "resolution_x";
    public static final String RESOLUTION_Y = "resolution_y";

    public static final String MODEL = "model";
    public static final String FORECAST_REFERENCE_TIME = "forecast_reference_time";
    public static final String PHENOMENON = "phenomenon";
    public static final String DATA_DIMENSIONS = "data_dimensions";

    public static final String DATA_DIMENSION_X = "data_dimension_x";
    public static final String DATA_DIMENSION_Y = "data_dimension_y";
    public static final String DATA_DIMENSION_Z = "data_dimension_z";

    public static final String FORECAST_TIME = "forecast_time";

    /*
     * Util
     */
    public static final String MIN = "min";
    public static final String MAX = "max";

    /*
     * Geographic
     */
    public static final String GEOGRAPHIC_REGION = "geographic_region";

    public static final String LAT = "lat";
    public static final String LNG = "lng";

    public static final String MIN_LAT_MIN_LNG = separateWithUnderscores(MIN, LAT, MIN, LNG);
    public static final String MIN_LAT_MAX_LNG = separateWithUnderscores(MIN, LAT, MAX, LNG);
    public static final String MAX_LAT_MAX_LNG = separateWithUnderscores(MAX, LAT, MAX, LNG);
    public static final String MAX_LAT_MIN_LNG = separateWithUnderscores(MAX, LAT, MIN, LNG);


    public static String separateWithUnderscores(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
            sb.append("_");
        }
        sb.deleteCharAt(sb.length());
        return sb.toString();
    }

}
