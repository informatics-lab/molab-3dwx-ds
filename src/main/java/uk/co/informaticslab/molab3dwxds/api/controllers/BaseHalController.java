package uk.co.informaticslab.molab3dwxds.api.controllers;

import uk.co.informaticslab.molab3dwxds.api.representations.MyRepresentationFactory;
import uk.co.informaticslab.molab3dwxds.api.utils.UriResolver;

/**
 * Created by tom on 29/05/2015.
 */
public abstract class BaseHalController implements HalController {

    protected final MyRepresentationFactory representationFactory;
    protected final UriResolver uriResolver;

    public BaseHalController(MyRepresentationFactory representationFactory,
                             UriResolver uriResolver) {
        this.representationFactory = representationFactory;
        this.uriResolver = uriResolver;
    }

}
