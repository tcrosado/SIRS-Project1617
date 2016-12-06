package pt.ulisboa.tecnico.sirs.t07.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.sirs.t07.data.*;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InsufficientFundsException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InvalidIbanException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ReplayException;
import pt.ulisboa.tecnico.sirs.t07.utils.UDPConnection;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by trosado on 15/11/16.
 */
public class TransferService extends OperationService {
    private final Logger logger = LoggerFactory.getLogger(TransferService.class);
    private  String tid;
    private String ibanDestination;
    private int value;
    private String result;

    public TransferService(UUID tid, String ibanOrigin, String ibanDestination, int value) {
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
        PendingTransactionsData pending = new PendingTransactionsData();
        Vector<Integer> result = accountdb.getBalanceFromIBAN(this.getIbanOrigin());

        if (result.isEmpty()) {
            logger.debug("Invalid Origin Iban {}.", this.getIbanOrigin());
            this.result = "II";
            throw new InvalidIbanException(this.getIbanOrigin());
        }

        if (result.firstElement() < this.value) {
            logger.debug("Insufficient funds.");
            this.result = "IF";
            throw new InsufficientFundsException(this.getIbanOrigin());
        }


        if (!client.ibanExists(this.ibanDestination)) {
            logger.debug("Invalid destination Iban.");
            this.result = "II";
            throw new InvalidIbanException(this.ibanDestination);
        }

       if (this.value > 5000){
           Vector<MatrixPosition> positions = new Vector<MatrixPosition>();
           this.result="TP";
           for(int i = 0; i<3;i++){
               GetMatrixRequestService service = new GetMatrixRequestService(this.getIbanOrigin());
               service.dispatch();
               this.result += "-"+service.result();
               String[] splited = service.result().split("-");
               String row = splited[0];
               Integer col = Integer.parseInt(splited[1]);
               Integer position = Integer.parseInt(splited[2]);
               positions.add(new MatrixPosition(row,col,position));
           }
           this.result += "-" + this.tid;
           try {
                pending.addPendingTransaction(this.tid,this.getIbanOrigin(),this.ibanDestination,this.value,positions);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            logger.debug("Transaction added to pending");
            return;

        }


        try {
            history.doTransaction(this.tid, this.getIbanOrigin(), this.ibanDestination, this.value);
        } catch (SQLIntegrityConstraintViolationException e){
            logger.info("Operation "+this.tid+" was replayed");
            throw new ReplayException(e);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.debug("Transfer Completed");
        this.result="TC";
    }

    @Override
    public String result() {
        return this.result;
    }
}
