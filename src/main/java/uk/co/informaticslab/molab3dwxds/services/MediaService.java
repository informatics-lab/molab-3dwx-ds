package uk.co.informaticslab.molab3dwxds.services;

import org.joda.time.DateTime;
import uk.co.informaticslab.molab3dwxds.api.params.ForecastTimeRange;
import uk.co.informaticslab.molab3dwxds.domain.Image;
import uk.co.informaticslab.molab3dwxds.domain.Media;
import uk.co.informaticslab.molab3dwxds.domain.Video;

import java.util.List;
import java.util.Optional;

/**
 * Created by tom on 29/05/2015.
 */
public interface MediaService {

    List<String> getModels();

    List<DateTime> getForecastReferenceTimes(String model);

    int countImages(String model, DateTime forecastReferenceTime);

    int countVideos(String model, DateTime forecastReferenceTime);

    Optional<? extends Media> insert(Media media);

    Optional<? extends Media> getById(String id, boolean withData);

    void deleteById(String id);

    Iterable<Image> getImagesByFilter(String phenomenon, ForecastTimeRange forecastTimeRange);

    Iterable<Video> getVideosByFilter(String phenomenon);

}
