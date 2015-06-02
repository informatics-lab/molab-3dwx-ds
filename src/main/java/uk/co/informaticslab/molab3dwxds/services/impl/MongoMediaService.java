package uk.co.informaticslab.molab3dwxds.services.impl;

import com.mysema.query.types.Predicate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.co.informaticslab.molab3dwxds.api.params.ForecastTimeRange;
import uk.co.informaticslab.molab3dwxds.domain.Image;
import uk.co.informaticslab.molab3dwxds.domain.Media;
import uk.co.informaticslab.molab3dwxds.domain.Video;
import uk.co.informaticslab.molab3dwxds.services.MediaService;
import uk.co.informaticslab.molab3dwxds.services.repositories.ImageRepository;
import uk.co.informaticslab.molab3dwxds.services.repositories.VideoRepository;
import uk.co.informaticslab.molab3dwxds.services.repositories.query.ImagePredicateBuilder;
import uk.co.informaticslab.molab3dwxds.services.repositories.query.PredicateBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MongoMediaService implements MediaService {

    private final ImageRepository imageRepository;
    private final VideoRepository videoRepository;

    @Autowired
    public MongoMediaService(ImageRepository imageRepository, VideoRepository videoRepository) {
        this.imageRepository = imageRepository;
        this.videoRepository = videoRepository;
    }


    @Override
    public List<String> getModels() {
        final List<String> uniqueModels = new ArrayList<>();
        for (Image image : imageRepository.findAllImageMeta()) {
            if (!uniqueModels.contains(image.getModel())) {
                uniqueModels.add(image.getModel());
            }
        }
        for (Video video : videoRepository.findAllVideoMeta()) {
            if (!uniqueModels.contains(video.getModel())) {
                uniqueModels.add(video.getModel());
            }
        }
        return uniqueModels;
    }

    @Override
    public List<String> getPhenomenons(String model, DateTime forecastReferenceTime) {
        final List<String> uniquePhenomenons = new ArrayList<>();
        for (Image image : imageRepository.findAllImageMetaByModelAndForecastReferenceTime(model, forecastReferenceTime)) {
            if (!uniquePhenomenons.contains(image.getPhenomenon())) {
                uniquePhenomenons.add(image.getPhenomenon());
            }
        }
        for (Video video : videoRepository.findAllVideoMetaByModelAndForecastReferenceTime(model, forecastReferenceTime)) {
            if (!uniquePhenomenons.contains(video.getPhenomenon())) {
                uniquePhenomenons.add(video.getPhenomenon());
            }
        }
        return uniquePhenomenons;
    }

    @Override
    public List<DateTime> getForecastReferenceTimes(String model) {

        final List<DateTime> uniqueForecastReferenceTimes = new ArrayList<>();
        for (Image image : imageRepository.findAllImageMeta()) {
            if (!uniqueForecastReferenceTimes.contains(image.getForecastReferenceTime())) {
                uniqueForecastReferenceTimes.add(image.getForecastReferenceTime());
            }
        }
        for (Video video : videoRepository.findAllVideoMeta()) {
            if (!uniqueForecastReferenceTimes.contains(video.getForecastReferenceTime())) {
                uniqueForecastReferenceTimes.add(video.getForecastReferenceTime());
            }
        }

        Collections.sort(uniqueForecastReferenceTimes);

        return uniqueForecastReferenceTimes;
    }

    @Override
    public int countImages(String model, DateTime forecastReferenceTime, String phenomenon) {
        return imageRepository.countByModelAndForecastReferenceTimeAndPhenomenon(model, forecastReferenceTime, phenomenon);
    }

    @Override
    public int countVideos(String model, DateTime forecastReferenceTime, String phenomenon) {
        return videoRepository.countByModelAndForecastReferenceTimeAndPhenomenon(model, forecastReferenceTime, phenomenon);
    }

    @Override
    public Optional<? extends Media> insert(Media media) {
        if (media instanceof Image) {
            Image image = (Image) media;
            return Optional.of(imageRepository.insert(image));
        } else if (media instanceof Video) {
            Video video = (Video) media;
            return Optional.of(videoRepository.insert(video));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<? extends Media> getById(String id, boolean withData) {
        if (imageRepository.exists(id)) {
            Image image;
            if (withData) {
                image = imageRepository.findOne(id);
            } else {
                image = imageRepository.findImageMetaById(id);
            }
            return Optional.of(image);
        } else if (videoRepository.exists(id)) {
            Video video;
            if (withData) {
                video = videoRepository.findOne(id);
            } else {
                video = videoRepository.findVideoMetaById(id);
            }
            return Optional.of(video);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void deleteById(String id) {
        if (imageRepository.exists(id)) {
            imageRepository.delete(id);
        } else if (videoRepository.exists(id)) {
            videoRepository.delete(id);
        }
    }

    @Override
    public Iterable<Image> getImagesByFilter(String model, DateTime forecastReferenceTime, String phenomenon, ForecastTimeRange forecastTimeRange) {
        PredicateBuilder builder = new ImagePredicateBuilder(model, forecastReferenceTime, phenomenon, forecastTimeRange);
        Predicate predicate = builder.buildPredicate();
        return imageRepository.findAllImageMetaByPredicate(predicate);
    }

    @Override
    public Iterable<Video> getVideosByFilter(String model, DateTime forecastReferenceTime, String phenomenon) {
        return videoRepository.findAllVideoMetaByModelAndForecastReferenceTimeAndPhenomenon(model, forecastReferenceTime, phenomenon);
    }

}
