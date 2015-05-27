package uk.co.informaticslab.molab3dwxds.api.caching.filters;

import uk.co.informaticslab.molab3dwxds.exceptions.TokenizedRuntimeException;

/**
 * Created by tom on 22/05/2015.
 */
public class FilterException extends TokenizedRuntimeException {

    /**
     * Constructor
     *
     * @param format  A tokenised message format (e.g. "There are {} failures.")
     * @param varargs The arguments to use to replace the tokens ("{}") one per token
     */
    public FilterException(String format, Object... varargs) {
        super(format, varargs);
    }

}
