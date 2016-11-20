package pt.ulisboa.tecnico.sirs.t07.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.sirs.t07.data.AccountData;
import pt.ulisboa.tecnico.sirs.t07.data.CustomerData;
import pt.ulisboa.tecnico.sirs.t07.data.TransferHistoryData;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InsufficientFundsException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InvalidIbanException;

import java.util.UUID;
import java.util.Vector;

/**
 * Created by trosado on 15/11/16.
 */
public class TransferService extends OperationService {
    private final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private  String tid;
    private String ibanDestination;
    private double value;
    private String result;

    public TransferService(UUID tid, String ibanOrigin, String ibanDestination, double value) {
        super(ibanOrigin);
        this.ibanDestination = ibanDestination;
        this.value = value;
        this.tid=tid.toString();
        this.result = "Not Executed";
    }

    @Override
    void dispatch() throws ErrorMessageException {
        AccountData accountdb = new AccountData();
        CustomerData client = new CustomerData();
        TransferHistoryData history = new TransferHistoryData();
        Vector<Float> result =  accountdb.getBalanceFromIBAN(this.getIbanOrigin());

        if(result.isEmpty()) {
            logger.debug("Invalid Origin Iban {}.",this.getIbanOrigin());
            this.result="Invalid Origin Iban";
            throw new InvalidIbanException(this.getIbanOrigin());
        }

        if(result.firstElement()<this.value) {
            logger.debug("Insufficient funds.");
            this.result="Insufficient funds";
            throw new InsufficientFundsException(this.getIbanOrigin());
        }


        if(!client.ibanExists(this.ibanDestination)){
            logger.debug("Invalid destination Iban.");
            this.result="Invalid destination Iban";
            throw new InvalidIbanException(this.ibanDestination);
        }

        try {
            history.doTransaction(this.tid,this.getIbanOrigin(),this.ibanDestination,this.value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("Transfer Completed");
        this.result="Transfer Completed";
    }

    @Override
    public String result() {
        return this.result;
    }
}
