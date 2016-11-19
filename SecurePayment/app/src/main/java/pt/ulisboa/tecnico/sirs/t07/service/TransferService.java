package pt.ulisboa.tecnico.sirs.t07.service;

import pt.ulisboa.tecnico.sirs.t07.data.AccountData;
import pt.ulisboa.tecnico.sirs.t07.data.CustomerData;
import pt.ulisboa.tecnico.sirs.t07.data.TransferHistoryData;

import java.util.UUID;
import java.util.Vector;

/**
 * Created by trosado on 15/11/16.
 */
public class TransferService extends OperationService {
    private  String tid;
    private String ibanDestination;
    private double value = 0;

    public TransferService(UUID tid, String ibanOrigin, String ibanDestination, double value) {
        super(ibanOrigin);
        this.ibanDestination = ibanDestination;
        this.value = value;
        this.tid=tid.toString();
    }

    @Override
    void dispatch() {
        AccountData accountdb = new AccountData();
        CustomerData client = new CustomerData();
        TransferHistoryData history = new TransferHistoryData();
        Vector<Float> result =  accountdb.getBalanceFromIBAN(this.getIbanOrigin());

        if (result.isEmpty())
            // FIXME: 17/11/16 Add exception
            return;

        if(result.firstElement()<this.value)
            // FIXME: 17/11/16 Add exception
            return;

        if(!client.ibanExists(this.ibanDestination))
            // FIXME: 17/11/16 Add exception
            return;


        try {
            history.doTransaction(this.tid,this.getIbanOrigin(),this.ibanDestination,this.value);
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.print("Transfer");
    }
}
