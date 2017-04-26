package pt.ulisboa.tecnico.sirs.t07.exceptions;

/**
 * Created by tiago on 20/11/2016.
 */
public class InsufficientFundsException extends ErrorMessageException {
    private String iban;
    public InsufficientFundsException(String iban){
        this.iban=iban;
    }

    @Override
    public String getMessage(){
        return "TF";
    }
}
