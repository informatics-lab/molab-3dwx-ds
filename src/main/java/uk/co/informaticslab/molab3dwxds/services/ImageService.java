package uk.co.informaticslab.molab3dwxds.services;

import uk.co.informaticslab.molab3dwxds.api.params.ForecastDTRange;
import uk.co.informaticslab.molab3dwxds.api.params.ModelRunDTRange;
import uk.co.informaticslab.molab3dwxds.domain.Image;
import uk.co.informaticslab.molab3dwxds.domain.Phenomenon;

/**
 * Created by tom on 26/05/2015.
 */
public interface ImageService {

    Image insert(Image image);

    Image getById(String id, boolean withData);

    void deleteById(String id);

    Iterable<Image> getByFilter(Phenomenon phenomenon, ModelRunDTRange modelRunDTRange, ForecastDTRange forecastDTRange);

}
