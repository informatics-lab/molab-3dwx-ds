package uk.co.informaticslab.molab3dwxds.services.repositories;

import org.joda.time.DateTime;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.co.informaticslab.molab3dwxds.domain.Video;

/**
 * MongoDB video repository
 * <p>
 * Makes use of 'spring-data query creation from method names' hence the terrible method names.
 */
public interface VideoRepository extends MongoRepository<Video, String>, QueryDslPredicateExecutor<Video> {

    @Query(value = "{ '_id' : ?0 }", fields = "{ 'data' : 0 }")
    Video findVideoMetaById(String id);

    @Query(value = "{}", fields = "{ 'data' : 0 }")
    Iterable<Video> findAllVideoMeta();

    @Query(value = "{ 'model' : ?0, 'forecastReferenceTime' : ?1, 'phenomenon' : ?2, 'processingProfile' : ?3 }", fields = "{ 'data' : 0 }")
    Iterable<Video> findAllVideoMetaByModelAndForecastReferenceTimeAndPhenomenonAndProcessingProfile(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile);

    int countByModelAndForecastReferenceTimeAndPhenomenonAndProcessingProfile(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile);

}
