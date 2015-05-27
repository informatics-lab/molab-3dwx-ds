package uk.co.informaticslab.molab3dwxds.services.repositories.query;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.DateTimePath;
import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.api.params.ModelRunDTRange;
import uk.co.informaticslab.molab3dwxds.domain.Phenomenon;
import uk.co.informaticslab.molab3dwxds.domain.QVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs a single predicate for the given video variables
 */
public class VideoPredicateBuilder extends DTRangePredicateBuilder {

    private final Phenomenon phenomenon;
    private final ModelRunDTRange modelRunDTRange;

    public VideoPredicateBuilder(Phenomenon phenomenon, ModelRunDTRange modelRunDTRange) {
        this.phenomenon = phenomenon;
        this.modelRunDTRange = modelRunDTRange;
    }

    public Predicate buildPredicate() {

        List<BooleanExpression> booleanExpressions = new ArrayList<>();

        if (phenomenon != null) {
            booleanExpressions.add(createPhenomenonExpression(phenomenon));
        }

        if (modelRunDTRange.isDateRangeSet()) {
            booleanExpressions.add(createModelRunDTRangeExpression(modelRunDTRange));
        }

        return BooleanExpression.allOf(booleanExpressions.toArray(new BooleanExpression[0]));
    }

    private BooleanExpression createPhenomenonExpression(Phenomenon phenomenon) {
        return QVideo.video.phenomenon.eq(phenomenon);
    }

    private BooleanExpression createModelRunDTRangeExpression(ModelRunDTRange dtRangeParam) {
        DateTimePath<DateTime> path = QVideo.video.modelRunDT;
        return createDTRangeExpression(path, dtRangeParam);
    }

}
