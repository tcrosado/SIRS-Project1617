package pt.ulisboa.tecnico.sirs.t07.service;

import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;

/**
 * Created by tiago on 14/11/2016.
 */
abstract  public class OperationService {

    private String ibanOrigin;

    public OperationService(){}
    public OperationService(String iban){
        this.ibanOrigin =iban;
    }

    abstract void dispatch() throws ErrorMessageException;

    public void execute() throws ErrorMessageException {
        this.dispatch();
    }

    public String getIbanOrigin() {
        return ibanOrigin;
    }



}
