package uk.co.informaticslab.molab3dwxds.services.impl;

import com.mysema.query.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.informaticslab.molab3dwxds.api.params.ModelRunDTRange;
import uk.co.informaticslab.molab3dwxds.domain.Phenomenon;
import uk.co.informaticslab.molab3dwxds.domain.Video;
import uk.co.informaticslab.molab3dwxds.services.VideoService;
import uk.co.informaticslab.molab3dwxds.services.repositories.VideoRepository;
import uk.co.informaticslab.molab3dwxds.services.repositories.query.PredicateBuilder;
import uk.co.informaticslab.molab3dwxds.services.repositories.query.VideoPredicateBuilder;

/**
 * Created by tom on 26/05/2015.
 */
@Service
public class MongoVideoService implements VideoService {

    private final VideoRepository repository;

    @Autowired
    public MongoVideoService(VideoRepository repository) {
        this.repository = repository;
    }


    @Override
    public Video insert(Video video) {
        return repository.insert(video);
    }

    @Override
    public Video getById(String id, boolean withData) {
        if (withData) {
            return repository.findOne(id);
        } else {
            return repository.findVideoMetaById(id);
        }
    }

    @Override
    public Video getLatest() {
        return repository.findFirstVideoMetaByOrderByModelRunDTDesc();
    }

    @Override
    public Video getLatestByPhenomenon(Phenomenon phenomenon) {
        return repository.findFirstVideoMetaByPhenomenonOrderByModelRunDTDesc(phenomenon);
    }

    @Override
    public void deleteById(String id) {
        repository.delete(id);
    }

    public Iterable<Video> getByFilter(Phenomenon phenomenon, ModelRunDTRange modelRunDTRange) {
        PredicateBuilder builder = new VideoPredicateBuilder(phenomenon, modelRunDTRange);
        Predicate predicate = builder.buildPredicate();
        return repository.findAllVideoMeta(predicate);
    }
}
