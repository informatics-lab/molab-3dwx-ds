package uk.co.informaticslab.molab3dwxds.services.repositories;

import com.mysema.query.types.Predicate;
import org.joda.time.DateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.co.informaticslab.molab3dwxds.domain.Image;

/**
 * MongoDB images repository
 */
public interface ImageRepository extends MongoRepository<Image, String>, QueryDslPredicateExecutor<Image> {

    @Query(value = "{}", fields = "{ 'data' : 0 }")
    Image findImageMetaById(String id);

    @Query(value = "{}", fields = "{ 'data' : 0 }")
    Iterable<Image> findAllImageMeta();

    @Query(value = "{}", fields = "{ 'data' : 0 }")
    Iterable<Image> findAllImageMeta(Predicate predicate);

    @Query(value = "{}", fields = "{ 'data' : 0 }")
    Page<Image> findAllImageMeta(Predicate predicate, Pageable pageable);

    int countByModelAndForecastReferenceTimeAndPhenomenon(String model, DateTime forecastReferenceTime, String phenomenon);


}
