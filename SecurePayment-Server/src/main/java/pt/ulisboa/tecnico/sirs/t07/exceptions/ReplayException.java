package pt.ulisboa.tecnico.sirs.t07.exceptions;

/**
 * Created by tiago on 20/11/2016.
 */
public class ReplayException extends ErrorMessageException {

    public ReplayException(){}
    public ReplayException(Exception e){}

    @Override
    public String getMessage(){
        return "MR";
    }
}
