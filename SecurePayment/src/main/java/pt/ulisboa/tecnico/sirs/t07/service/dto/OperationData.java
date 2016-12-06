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

    public OperationData(UUID uid,OperationService service){
        this.operationUid=uid;
        this.service=service;
    }

    public void executeService() throws ErrorMessageException {
        this.service.execute();
    }

    public String getServiceResult(){
        return this.service.result();
    }
}
