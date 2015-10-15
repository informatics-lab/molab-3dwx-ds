package uk.co.informaticslab.molab3dwxds.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOptions;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.stereotype.Service;
import uk.co.informaticslab.molab3dwxds.domain.Video;

/**
 * Allows for aggregation queries on the MongoDB
 */
@Service
public class MongoAggregationMediaService {

    private static final Logger LOG = LoggerFactory.getLogger(MongoAggregationMediaService.class);

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoAggregationMediaService(MongoTemplate mongoTemplate) {
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

}
