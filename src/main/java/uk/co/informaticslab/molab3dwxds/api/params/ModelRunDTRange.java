package uk.co.informaticslab.molab3dwxds.api.params;

import javax.ws.rs.QueryParam;

public class ModelRunDTRange extends DTRange {

    private static final String MODEL_RUN_DT = "model-run-dt";

    public ModelRunDTRange(@QueryParam(MODEL_RUN_DT + "-" + GT) String dtRangeGT,
                           @QueryParam(MODEL_RUN_DT + "-" + GTE) String dtRangeGTE,
                           @QueryParam(MODEL_RUN_DT + "-" + LT) String dtRangeLT,
                           @QueryParam(MODEL_RUN_DT + "-" + LTE) String dtRangeLTE) {
        super(dtRangeGT, dtRangeGTE, dtRangeLT, dtRangeLTE);
    }
}