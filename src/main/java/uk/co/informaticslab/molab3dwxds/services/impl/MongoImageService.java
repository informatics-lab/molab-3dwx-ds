package uk.co.informaticslab.molab3dwxds.services.impl;

import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.informaticslab.molab3dwxds.api.params.ForecastDTRange;
import uk.co.informaticslab.molab3dwxds.api.params.ModelRunDTRange;
import uk.co.informaticslab.molab3dwxds.domain.Image;
import uk.co.informaticslab.molab3dwxds.domain.Phenomenon;
import uk.co.informaticslab.molab3dwxds.services.ImageService;
import uk.co.informaticslab.molab3dwxds.services.repositories.ImageRepository;
import uk.co.informaticslab.molab3dwxds.services.repositories.query.ImagePredicateBuilder;
import uk.co.informaticslab.molab3dwxds.services.repositories.query.PredicateBuilder;

/**
 * Created by tom on 26/05/2015.
 */
@Service
public class MongoImageService implements ImageService {

    private final ImageRepository repository;

    @Autowired
    public MongoImageService(ImageRepository repository) {
        this.repository = repository;
    }


    @Override
    public Image insert(Image image) {
        return repository.insert(image);
    }

    @Override
    public Image getById(String id, boolean withData) {
        if (withData) {
            return repository.findOne(id);
        } else {
            return repository.findImageMetaById(id);
        }
    }

    @Override
    public void deleteById(String id) {
        repository.delete(id);
    }


    public Iterable<Image> getByFilter(Phenomenon phenomenon, ModelRunDTRange modelRunDTRange, ForecastDTRange forecastDTRange) {
        PredicateBuilder builder = new ImagePredicateBuilder(phenomenon, modelRunDTRange, forecastDTRange);
        Predicate predicate = builder.buildPredicate();
        return repository.findAllImageMeta(predicate);
    }
}
