package uk.co.informaticslab.molab3dwxds.api.controllers;

import com.theoryinpractise.halbuilder.api.Representation;

import java.net.URI;

/**
 * Created by tom on 29/05/2015.
 */
public interface HalController {

    Representation getCapabilities();

    URI getSelf();

}
