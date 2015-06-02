package uk.co.informaticslab.molab3dwxds.services.repositories.query;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.DateTimePath;
import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.api.params.ForecastTimeRange;
import uk.co.informaticslab.molab3dwxds.domain.QImage;

import java.util.ArrayList;
import java.util.List;


/**
 * Constructs a single predicate for the given image variables
 */
public class ImagePredicateBuilder extends DTRangePredicateBuilder {

    private final String model;
    private final DateTime forecastReferenceTime;
    private final String phenomenon;
    private final ForecastTimeRange forecastTimeRange;

    public ImagePredicateBuilder(String model, DateTime forecastReferenceTime, String phenomenon, ForecastTimeRange forecastTimeRange) {
        this.model = model;
        this.forecastReferenceTime = forecastReferenceTime;
        this.phenomenon = phenomenon;
        this.forecastTimeRange = forecastTimeRange;
    }

    public Predicate buildPredicate() {

        List<BooleanExpression> booleanExpressions = new ArrayList<>();

        if (model != null) {
            booleanExpressions.add(createModelExpression(model));
        }

        if (forecastReferenceTime != null) {
            booleanExpressions.add(createForecastReferenceTimeExpression(forecastReferenceTime));
        }

        if (phenomenon != null) {
            booleanExpressions.add(createPhenomenonExpression(phenomenon));
        }

        if (forecastTimeRange.isDateRangeSet()) {
            booleanExpressions.add(createForecastTimeRangeExpression(forecastTimeRange));
        }

        return BooleanExpression.allOf(booleanExpressions.toArray(new BooleanExpression[0]));
    }

    private BooleanExpression createModelExpression(String model) {
        return QImage.image.model.eq(model);
    }

    private BooleanExpression createForecastReferenceTimeExpression(DateTime forecastReferenceTime) {
        return QImage.image.forecastReferenceTime.eq(forecastReferenceTime);
    }

    private BooleanExpression createPhenomenonExpression(String phenomenon) {
        return QImage.image.phenomenon.eq(phenomenon);
    }

    private BooleanExpression createForecastTimeRangeExpression(ForecastTimeRange dtRangeParam) {
        DateTimePath<DateTime> path = QImage.image.forecastTime;
        return createDTRangeExpression(path, dtRangeParam);
    }

}
