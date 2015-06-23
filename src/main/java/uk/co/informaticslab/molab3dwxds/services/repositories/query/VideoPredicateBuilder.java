package uk.co.informaticslab.molab3dwxds.services.repositories.query;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.domain.QVideo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 03/06/2015.
 */
public class VideoPredicateBuilder implements PredicateBuilder {

    private final String model;
    private final DateTime forecastReferenceTime;
    private final String phenomenon;
    private final String processingProfile;

    public VideoPredicateBuilder(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile) {
        this.model = model;
        this.forecastReferenceTime = forecastReferenceTime;
        this.phenomenon = phenomenon;
        this.processingProfile = processingProfile;
    }

    @Override
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

        if (processingProfile != null) {
            booleanExpressions.add(createProcessingProfileExpression(processingProfile));
        }

        return BooleanExpression.allOf(booleanExpressions.toArray(new BooleanExpression[0]));
    }

    private BooleanExpression createModelExpression(String model) {
        return QVideo.video.model.eq(model);
    }

    private BooleanExpression createForecastReferenceTimeExpression(DateTime forecastReferenceTime) {
        return QVideo.video.forecastReferenceTime.eq(forecastReferenceTime);
    }

    private BooleanExpression createPhenomenonExpression(String phenomenon) {
        return QVideo.video.phenomenon.eq(phenomenon);
    }

    private BooleanExpression createProcessingProfileExpression(String processingProfile) {
        return QVideo.video.processingProfile.eq(processingProfile);
    }

}
