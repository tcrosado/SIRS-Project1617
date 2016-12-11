package pt.ulisboa.tecnico.sirs.t07.exceptions;

/**
 * Created by tiago on 20/11/2016.
 */
public class InvalidOperationException extends ErrorMessageException {

    public InvalidOperationException(){}

    @Override
    public String getMessage(){
        return "IO";
    }
}
