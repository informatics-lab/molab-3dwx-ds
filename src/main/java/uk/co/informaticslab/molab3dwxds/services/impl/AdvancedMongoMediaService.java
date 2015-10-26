package uk.co.informaticslab.molab3dwxds.services.impl;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import uk.co.informaticslab.molab3dwxds.api.params.DTRange;
import uk.co.informaticslab.molab3dwxds.api.params.ForecastTimeRange;
import uk.co.informaticslab.molab3dwxds.domain.Image;
import uk.co.informaticslab.molab3dwxds.domain.Video;

/**
 * Allows for aggregation queries on the MongoDB
 */
@Service
public class AdvancedMongoMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(AdvancedMongoMediaService.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public AdvancedMongoMediaService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    /**
     * @return the latest Video for each phenomenon currently in the DB
     */
    public Iterable<Video> getLatestVideosGroupedByPhenomenon() {

        TypedAggregation<Video> aggregation = Aggregation.newAggregation(Video.class,
                Aggregation.sort(Sort.Direction.DESC, "forecastReferenceTime"),
                Aggregation.group("phenomenon", "model", "processingProfile").first("$$ROOT").as("result"),
                Aggregation.project()
                        .andExclude("_id")
                        .and("result._id").as("_id")
                        .and("result.dataDimensions").as("dataDimensions")
                        .and("result.forecastReferenceTime").as("forecastReferenceTime")
                        .and("result.geographicRegion").as("geographicRegion")
                        .and("result.model").as("model")
                        .and("result.phenomenon").as("phenomenon")
                        .and("result.processingProfile").as("processingProfile")
                        .and("result.resolution").as("resolution")
        );

        AggregationOptions opts = new AggregationOptions.Builder().allowDiskUse(true).build();

        return mongoTemplate.aggregate(aggregation.withOptions(opts), Video.class).getMappedResults();
    }

    public Iterable<Image> findAllImageMetaByFilter(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile, ForecastTimeRange forecastTimeRange) {
        LOG.debug("calling");
        Query q = new Query();
        if (model != null) {
            q.addCriteria(Criteria.where("model").is(model));
        }
        if (forecastReferenceTime != null) {
            q.addCriteria(Criteria.where("forecastReferenceTime").is(forecastReferenceTime));
        }
        if (phenomenon != null) {
            q.addCriteria(Criteria.where("phenomenon").is(phenomenon));
        }
        if (processingProfile != null) {
            q.addCriteria(Criteria.where("processingProfile").is(processingProfile));
        }
        if (forecastTimeRange != null && forecastTimeRange.isDateRangeSet()) {
            q.addCriteria(getCriteriaDefinitionForDTRange("forecastTime", forecastTimeRange));
        }

        q.fields().exclude("data");
        q.with(new Sort(Sort.Direction.ASC, "forecastTime"));

        return mongoTemplate.find(q, Image.class);

    }

    public Iterable<Video> findAllVideoMetaByFilter(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile) {
        LOG.debug("calling");
        Query q = new Query();
        if (model != null) {
            q.addCriteria(Criteria.where("model").is(model));
        }
        if (forecastReferenceTime != null) {
            q.addCriteria(Criteria.where("forecastReferenceTime").is(forecastReferenceTime));
        }
        if (phenomenon != null) {
            q.addCriteria(Criteria.where("phenomenon").is(phenomenon));
        }
        if (processingProfile != null) {
            q.addCriteria(Criteria.where("processingProfile").is(processingProfile));
        }

        q.fields().exclude("data");
        q.with(new Sort(Sort.Direction.ASC, "forecastTime"));

        return mongoTemplate.find(q, Video.class);

    }

    private CriteriaDefinition getCriteriaDefinitionForDTRange(String dtKey, DTRange dtRange) {
        Criteria c = new Criteria(dtKey);
        if (dtRange.isGt()) {
            c.gt(dtRange.getLowerBound());
        }
        if (dtRange.isGte()) {
            c.gte(dtRange.getLowerBound());
        }
        if (dtRange.isLt()) {
            c.lt(dtRange.getUpperBound());
        }
        if (dtRange.isLte()) {
            c.lte(dtRange.getUpperBound());
        }
        return c;
    }


}
