package uk.co.informaticslab.molab3dwxds.services.repositories.query;

import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.DateTimePath;
import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.api.params.DTRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides helpers to construct date-time range predicates.
 */
public abstract class DTRangePredicateBuilder implements PredicateBuilder {

    protected BooleanExpression createDTRangeExpression(DateTimePath<DateTime> path, DTRange dtRange) {
        List<BooleanExpression> booleanExpressions = new ArrayList<>();

        if (dtRange.isGt() || dtRange.isGte()) {
            booleanExpressions.add(path.after(dtRange.getLowerBound()));
        }
        if (dtRange.isGte()) {
            booleanExpressions.add(path.eq(dtRange.getLowerBound()));
        }
        if (dtRange.isLt() || dtRange.isLte()) {
            booleanExpressions.add(path.before(dtRange.getUpperBound()));
        }
        if (dtRange.isLte()) {
            booleanExpressions.add(path.eq(dtRange.getUpperBound()));
        }
        return BooleanExpression.allOf(booleanExpressions.toArray(new BooleanExpression[0]));
    }

}
