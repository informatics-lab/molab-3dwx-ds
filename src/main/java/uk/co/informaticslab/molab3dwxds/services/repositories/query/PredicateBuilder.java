package uk.co.informaticslab.molab3dwxds.services.repositories.query;

import com.mysema.query.types.Predicate;

/**
 * Predicate builder interface
 */
public interface PredicateBuilder {

    Predicate buildPredicate();

}
