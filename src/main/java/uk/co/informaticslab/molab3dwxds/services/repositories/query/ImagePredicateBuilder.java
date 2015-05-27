package uk.co.informaticslab.molab3dwxds.services.repositories.query;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.DateTimePath;
import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.api.params.ForecastDTRange;
import uk.co.informaticslab.molab3dwxds.api.params.ModelRunDTRange;
import uk.co.informaticslab.molab3dwxds.domain.Phenomenon;
import uk.co.informaticslab.molab3dwxds.domain.QImage;

import java.util.ArrayList;
import java.util.List;


/**
 * Constructs a single predicate for the given image variables
 */
public class ImagePredicateBuilder extends DTRangePredicateBuilder {

    private final Phenomenon phenomenon;
    private final ModelRunDTRange modelRunDTRange;
    private final ForecastDTRange forecastDTRange;

    public ImagePredicateBuilder(Phenomenon phenomenon, ModelRunDTRange modelRunDTRange, ForecastDTRange forecastDTRange) {
        this.phenomenon = phenomenon;
        this.modelRunDTRange = modelRunDTRange;
        this.forecastDTRange = forecastDTRange;
    }

    public Predicate buildPredicate() {

        List<BooleanExpression> booleanExpressions = new ArrayList<>();

        if (phenomenon != null) {
            booleanExpressions.add(createPhenomenonExpression(phenomenon));
        }

        if (modelRunDTRange.isDateRangeSet()) {
            booleanExpressions.add(createModelRunDTRangeExpression(modelRunDTRange));
        }

        if (forecastDTRange.isDateRangeSet()) {
            booleanExpressions.add(createForecastDTRangeExpression(forecastDTRange));
        }

        return BooleanExpression.allOf(booleanExpressions.toArray(new BooleanExpression[0]));
    }

    private BooleanExpression createPhenomenonExpression(Phenomenon phenomenon) {
        return QImage.image.phenomenon.eq(phenomenon);
    }

    private BooleanExpression createModelRunDTRangeExpression(ModelRunDTRange dtRangeParam) {
        DateTimePath<DateTime> path = QImage.image.modelRunDT;
        return createDTRangeExpression(path, dtRangeParam);
    }

    private BooleanExpression createForecastDTRangeExpression(ForecastDTRange dtRangeParam) {
        DateTimePath<DateTime> path = QImage.image.forecastDT;
        return createDTRangeExpression(path, dtRangeParam);
    }

}
