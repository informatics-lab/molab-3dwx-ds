package uk.co.informaticslab.molab3dwxds.api.params;

import javax.ws.rs.QueryParam;

public class ForecastDTRange extends DTRange {

    public static final String FORECAST_DT = "forecast-dt";

    public ForecastDTRange(@QueryParam(FORECAST_DT + "-" + GT) String dtRangeGT,
                           @QueryParam(FORECAST_DT + "-" + GTE) String dtRangeGTE,
                           @QueryParam(FORECAST_DT + "-" + LT) String dtRangeLT,
                           @QueryParam(FORECAST_DT + "-" + LTE) String dtRangeLTE) {
        super(dtRangeGT, dtRangeGTE, dtRangeLT, dtRangeLTE);
    }
}