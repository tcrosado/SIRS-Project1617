package pt.ulisboa.tecnico.sirs.t07.exceptions;

import java.util.UUID;

/**
 * Created by tiago on 20/11/2016.
 */
public class InvalidHashException extends ErrorMessageException {
    private UUID tid;
    public InvalidHashException(UUID tid){
        this.tid = tid;
    }


    @Override
    public String getMessage(){

        return "IH";
    }
}
