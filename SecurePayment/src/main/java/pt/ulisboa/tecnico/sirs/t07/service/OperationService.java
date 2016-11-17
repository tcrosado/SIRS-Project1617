package pt.ulisboa.tecnico.sirs.t07.service;

/**
 * Created by tiago on 14/11/2016.
 */
abstract  public class OperationService extends AbstractService {

    private String ibanOrigin;

    public OperationService(String iban){
        this.ibanOrigin =iban;
    }

    public String getIbanOrigin() {
        return ibanOrigin;
    }



}
