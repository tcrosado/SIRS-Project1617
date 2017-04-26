package pt.ulisboa.tecnico.sirs.t07.service.dto;


import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.service.OperationService;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by tiago on 14/11/2016.
 */
public class OperationData {
    private UUID operationUid;
    private OperationService service;
    private String phoneNumber;

    private OperationData(){}
    public OperationData(String phoneNumber) {
        this.phoneNumber=phoneNumber;
    }

    public OperationData(UUID uid, String phoneNumber, OperationService service){
        this.operationUid=uid;
        this.service=service;
        this.phoneNumber=phoneNumber;
    }

    public void executeService() throws ErrorMessageException {
        this.service.execute();
    }

    public String getServiceResult(){
        return this.service.result();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}
