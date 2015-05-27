package uk.co.informaticslab.molab3dwxds.exceptions;

/**
 * Created by tom on 18/05/2015.
 */
public class DTRangeException extends TokenizedRuntimeException {

    /**
     * Constructor
     *
     * @param format  A tokenised message format (e.g. "There are {} failures.")
     * @param varargs The arguments to use to replace the tokens ("{}") one per token
     */
    public DTRangeException(String format, Object... varargs) {
        super(format, varargs);
    }
}
