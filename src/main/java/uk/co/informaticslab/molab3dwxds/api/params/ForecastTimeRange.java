package uk.co.informaticslab.molab3dwxds.api.params;

import uk.co.informaticslab.molab3dwxds.domain.Constants;

import javax.ws.rs.QueryParam;

public class ForecastTimeRange extends DTRange {

    public ForecastTimeRange(@QueryParam(Constants.FORECAST_TIME + "_" + GT) String dtRangeGT,
                             @QueryParam(Constants.FORECAST_TIME + "_" + GTE) String dtRangeGTE,
                             @QueryParam(Constants.FORECAST_TIME + "_" + LT) String dtRangeLT,
                             @QueryParam(Constants.FORECAST_TIME + "_" + LTE) String dtRangeLTE) {
        super(dtRangeGT, dtRangeGTE, dtRangeLT, dtRangeLTE);
    }
}