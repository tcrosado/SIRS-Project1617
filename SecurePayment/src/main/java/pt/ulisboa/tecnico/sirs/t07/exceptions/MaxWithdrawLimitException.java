package pt.ulisboa.tecnico.sirs.t07.exceptions;

/**
 * Created by trosado on 30/11/16.
 */
public class MaxWithdrawLimitException extends ErrorMessageException {

    private String iban;

    public MaxWithdrawLimitException(String iban){
        this.iban = iban;
    }

    @Override
    public String getMessage() {
        return "MD";
    }
}
