package pt.ulisboa.tecnico.sirs.t07.service;

/**
 * Created by tiago on 14/11/2016.
 */
abstract  public class OperationService extends AbstractService {

    private String originIban;

    public OperationService(String iban){
        this.originIban=iban;
    }

    public String getOriginIban() {
        return originIban;
    }



}
