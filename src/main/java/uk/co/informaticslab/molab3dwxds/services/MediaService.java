package uk.co.informaticslab.molab3dwxds.services;

import org.joda.time.DateTime;
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

    List<String> getPhenomenons(String model, DateTime forecastReferenceTime);

    List<String> getProcessingProfiles(String model, DateTime forecastReferenceTime, String phenomenon);

    int countImages(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile);

    int countVideos(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile);

    int countAllVideos();

    Optional<? extends Media> insert(Media media);

    Optional<? extends Media> getById(String id, boolean withData);

    void deleteById(String id);

    void deleteAll();

    Iterable<Video> getVideosByFilter(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile);

    Optional<DateTime> getLatestForecastReferenceTime(String model);

}
