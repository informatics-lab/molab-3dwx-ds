package uk.co.informaticslab.molab3dwxds.services.impl;

import com.mysema.query.types.Predicate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import uk.co.informaticslab.molab3dwxds.services.repositories.query.VideoPredicateBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MongoMediaService implements MediaService {

    private static final Logger LOG = LoggerFactory.getLogger(MongoMediaService.class);

    private static final Pageable LATEST_FORECAST_REFERENCE_TIME = new PageRequest(0, 1, Sort.Direction.DESC, "forecastReferenceTime");

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
    public List<String> getPhenomenons(String model, DateTime forecastReferenceTime) {
        final List<String> uniquePhenomenons = new ArrayList<>();
        ImagePredicateBuilder imagePredicateBuilder = new ImagePredicateBuilder(model, forecastReferenceTime, null, null, null);
        for (Image image : imageRepository.findAll(imagePredicateBuilder.buildPredicate())) {
            if (!uniquePhenomenons.contains(image.getPhenomenon())) {
                uniquePhenomenons.add(image.getPhenomenon());
            }
        }
        VideoPredicateBuilder videoPredicateBuilder = new VideoPredicateBuilder(model, forecastReferenceTime, null, null);
        for (Video video : videoRepository.findAll(videoPredicateBuilder.buildPredicate())) {
            if (!uniquePhenomenons.contains(video.getPhenomenon())) {
                uniquePhenomenons.add(video.getPhenomenon());
            }
        }
        return uniquePhenomenons;
    }

    @Override
    public List<String> getProcessingProfiles(String model, DateTime forecastReferenceTime, String phenomenon) {
        final List<String> uniqueProcessingProfiles = new ArrayList<>();

        ImagePredicateBuilder imagePredicateBuilder = new ImagePredicateBuilder(model, forecastReferenceTime, phenomenon, null, null);

        for (Image image : imageRepository.findAll(imagePredicateBuilder.buildPredicate())) {
            if (!uniqueProcessingProfiles.contains(image.getProcessingProfile())) {
                uniqueProcessingProfiles.add(image.getProcessingProfile());
            }
        }

        VideoPredicateBuilder videoPredicateBuilder = new VideoPredicateBuilder(model, forecastReferenceTime, phenomenon, null);

        for (Video video : videoRepository.findAll(videoPredicateBuilder.buildPredicate())) {
            if (!uniqueProcessingProfiles.contains(video.getProcessingProfile())) {
                uniqueProcessingProfiles.add(video.getProcessingProfile());
            }
        }

        return uniqueProcessingProfiles;
    }


    @Override
    public int countImages(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile) {
        return imageRepository.countByModelAndForecastReferenceTimeAndPhenomenonAndProcessingProfile(model, forecastReferenceTime, phenomenon, processingProfile);
    }

    @Override
    public int countVideos(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile) {
        return videoRepository.countByModelAndForecastReferenceTimeAndPhenomenonAndProcessingProfile(model, forecastReferenceTime, phenomenon, processingProfile);
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
    public void deleteAll() {
        imageRepository.deleteAll();
        videoRepository.deleteAll();
    }

    @Override
    public Iterable<Image> getImagesByFilter(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile, ForecastTimeRange forecastTimeRange) {
        PredicateBuilder builder = new ImagePredicateBuilder(model, forecastReferenceTime, phenomenon, processingProfile, forecastTimeRange);
        Predicate predicate = builder.buildPredicate();
        return imageRepository.findAll(predicate, ImagePredicateBuilder.orderByForcastTime());
    }

    @Override
    public Iterable<Video> getVideosByFilter(String model, DateTime forecastReferenceTime, String phenomenon, String processingProfile) {
        PredicateBuilder builder = new VideoPredicateBuilder(model, forecastReferenceTime, phenomenon, processingProfile);
        Predicate predicate = builder.buildPredicate();
        return videoRepository.findAll(predicate);
    }

    @Override
    public Optional<DateTime> getLatestForecastReferenceTime(String model) {
        ImagePredicateBuilder imagePredicateBuilder = new ImagePredicateBuilder(model, null, null, null, null);
        Page<Image> imagePage = imageRepository.findAll(imagePredicateBuilder.buildPredicate(), LATEST_FORECAST_REFERENCE_TIME);

        Image latestImage = null;
        if (imagePage.getContent().size() == 1) {
            latestImage = imagePage.getContent().get(0);
            LOG.debug("Latest image [ frt : {}, ft {} ]", latestImage.getForecastReferenceTime(), latestImage.getForecastTime());
        }

        VideoPredicateBuilder videoPredicateBuilder = new VideoPredicateBuilder(model, null, null, null);
        Page<Video> videoPage = videoRepository.findAll(videoPredicateBuilder.buildPredicate(), LATEST_FORECAST_REFERENCE_TIME);

        Video latestVideo = null;
        if (videoPage.getContent().size() == 1) {
            latestVideo = videoPage.getContent().get(0);
            LOG.debug("Latest video [ frt : {} ]", latestVideo.getForecastReferenceTime());
        }

        if (latestImage != null && latestVideo != null) {
            if (latestImage.getForecastReferenceTime().isAfter(latestVideo.getForecastReferenceTime())) {
                return Optional.of(latestImage.getForecastReferenceTime());
            } else {
                return Optional.of(latestVideo.getForecastReferenceTime());
            }
        } else if (latestImage != null) {
            return Optional.of(latestImage.getForecastReferenceTime());
        } else if (latestVideo != null) {
            return Optional.of(latestVideo.getForecastReferenceTime());
        } else {
            return Optional.empty();
        }
    }

}
