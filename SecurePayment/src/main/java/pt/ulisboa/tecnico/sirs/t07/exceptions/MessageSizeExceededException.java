package pt.ulisboa.tecnico.sirs.t07.exceptions;

/**
 * Created by trosado on 30/11/16.
 */
public class MessageSizeExceededException extends ErrorMessageException {
    @Override
    public String getMessage() {
        return "MS";
    }
}
