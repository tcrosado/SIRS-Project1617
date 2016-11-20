package pt.ulisboa.tecnico.sirs.t07.exceptions;

/**
 * Created by tiago on 20/11/2016.
 */
public class InvalidIbanException extends ErrorMessageException {

    private String iban;

    public InvalidIbanException(String iban){
        this.iban = iban;
    }

    @Override
    public String getMessage() {
        return "Iban: "+this.iban+" is invalid.";
    }
}
