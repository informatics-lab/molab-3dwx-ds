package uk.co.informaticslab.molab3dwxds.services.repositories;

import com.mysema.query.types.Predicate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import uk.co.informaticslab.molab3dwxds.domain.Image;

/**
 * MongoDB images repository
 */
public interface ImageRepository extends MongoRepository<Image, String>, QueryDslPredicateExecutor<Image> {

    @Query(value = "{}", fields = "{ 'data' : 0 }")
    Iterable<Image> findAllImageMeta(Predicate predicate);

    @Query(value = "{}", fields = "{ 'data' : 0 }")
    Image findImageMetaById(String id);

}
