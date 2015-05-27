package uk.co.informaticslab.molab3dwxds.api.params;

import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.exceptions.DTRangeException;

/**
 * Creates and validates a date-time range
 */
public abstract class DTRange {

    public static final String GT = "gt";
    public static final String GTE = "gte";
    public static final String LT = "lt";
    public static final String LTE = "lte";

    private DateTime lowerBound;
    private DateTime upperBound;

    private boolean gt = false;
    private boolean gte = false;
    private boolean lt = false;
    private boolean lte = false;

    /**
     * Constructs a valid date-time range
     *
     * @param dtRangeGT  date-time value greater than
     * @param dtRangeGTE date-time value greater than or equal to
     * @param dtRangeLT  date-time value less than
     * @param dtRangeLTE date-time value less than or equal to
     */
    public DTRange(String dtRangeGT, String dtRangeGTE, String dtRangeLT, String dtRangeLTE) {
        if (dtRangeGT != null && dtRangeGTE != null) {
            throw new DTRangeException("You may not specify both a 'greater than' & 'greater than and equal to' " +
                    "arguments in a valid date-time range.");
        } else if (dtRangeLT != null && dtRangeLTE != null) {
            throw new DTRangeException("You may not specify both a 'less than' & 'less than and equal to' " +
                    "arguments in a valid date-time range.");
        } else {
            setRangeArg(dtRangeGT, GT);
            setRangeArg(dtRangeGTE, GTE);
            setRangeArg(dtRangeLT, LT);
            setRangeArg(dtRangeLTE, LTE);
            validateRange();
        }
    }

    private void setRangeArg(String rangeValue, String idArg) {
        if (rangeValue != null) {
            switch (idArg) {
                case GT:
                    setGt(true);
                    setLowerBound(new DateTime(rangeValue));
                    return;
                case GTE:
                    setGte(true);
                    setLowerBound(new DateTime(rangeValue));
                    return;
                case LT:
                    setLt(true);
                    setUpperBound(new DateTime(rangeValue));
                    return;
                case LTE:
                    setLte(true);
                    setUpperBound(new DateTime(rangeValue));
                    return;
            }
        }
    }

    private void validateRange() {
        if (lowerBound != null && upperBound != null) {
            if (lowerBound.isAfter(upperBound)) {
                throw new DTRangeException("When specifying both a 'greater than' value and a 'less than' value in a " +
                        "date-time range, the 'less than' value must be exactly equal to or after the 'greater than value");
            }
        }
    }

    public boolean isDateRangeSet() {
        if (gt || gte || lt || lte) {
            return true;
        }
        return false;
    }

    public DateTime getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(DateTime lowerBound) {
        this.lowerBound = lowerBound;
    }

    public DateTime getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(DateTime upperBound) {
        this.upperBound = upperBound;
    }

    public boolean isLt() {
        return lt;
    }

    public void setLt(boolean lt) {
        this.lt = lt;
    }

    public boolean isLte() {
        return lte;
    }

    public void setLte(boolean lte) {
        this.lte = lte;
    }

    public boolean isGt() {
        return gt;
    }

    public void setGt(boolean gt) {
        this.gt = gt;
    }

    public boolean isGte() {
        return gte;
    }

    public void setGte(boolean gte) {
        this.gte = gte;
    }

    @Override
    public String toString() {
        return "DTRange{" +
                "lowerBound=" + lowerBound +
                ", upperBound=" + upperBound +
                ", gt=" + gt +
                ", gte=" + gte +
                ", lt=" + lt +
                ", lte=" + lte +
                '}';
    }
}
