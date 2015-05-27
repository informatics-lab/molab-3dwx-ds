package uk.co.informaticslab.molab3dwxds.services;

import uk.co.informaticslab.molab3dwxds.api.params.ModelRunDTRange;
import uk.co.informaticslab.molab3dwxds.domain.Phenomenon;
import uk.co.informaticslab.molab3dwxds.domain.Video;

/**
 * Created by tom on 26/05/2015.
 */
public interface VideoService {

    Video insert(Video video);

    Video getById(String id, boolean withData);

    Video getLatest();

    Video getLatestByPhenomenon(Phenomenon phenomenon);

    void deleteById(String id);

    Iterable<Video> getByFilter(Phenomenon phenomenon, ModelRunDTRange modelRunDTRange);

}
