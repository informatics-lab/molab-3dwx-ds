package uk.co.informaticslab.molab3dwxds.services.repositories;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.co.informaticslab.molab3dwxds.domain.Image;

/**
 * MongoDB images repository
 */
public interface ImageRepository extends MongoRepository<Image, String>, QueryDslPredicateExecutor<Image> {

    @Query(value = "{ '_id' : ?0 }", fields = "{ 'data' : 0 }")
    Image findImageMetaById(String id);

    @Query(value = "{}", fields = "{ 'data' : 0 }")
    Iterable<Image> findAllImageMeta();

    int countByModelAndForecastReferenceTimeAndPhenomenonAndProcessingProfile(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile);

}
