package pt.ulisboa.tecnico.sirs.t07.service;

import pt.ulisboa.tecnico.sirs.t07.data.AccountMatrixData;
import pt.ulisboa.tecnico.sirs.t07.data.MatrixPosition;
import pt.ulisboa.tecnico.sirs.t07.data.PendingTransactionsData;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;

import java.sql.SQLException;
import java.util.AbstractList;
import java.util.UUID;

/**
 * Created by trosado on 05/12/16.
 */
public class ConfirmTransactionService extends OperationService {

    private UUID tid;
    private AbstractList<Integer> matrixValues;
    private String result;
    ConfirmTransactionService(UUID tid, AbstractList<Integer> values){
        this.tid = tid;
        this.matrixValues = values;
    }

    @Override
    void dispatch() throws ErrorMessageException {
        Boolean secure = false;
        PendingTransactionsData pending = new PendingTransactionsData();
        AccountMatrixData matrixData = new AccountMatrixData();
        try {
            int i = 0;

            for(Integer value : matrixValues){
                MatrixPosition pos =  pending.getMatrixCoordenates(tid.toString(),i);
                String iban = pending.getIbanFromTid(tid.toString());
               if(!matrixData.verifyMatrixValue(iban,pos.getRow(),pos.getCol(),pos.getPos(),matrixValues.get(i++))){
                   secure = false;
                   break;
               }else
                   secure = true;

            }

            if (secure) {
                pending.confirmTransaction(tid.toString());
                this.result = "TC";
            } else {
                pending.abortTransaction(tid.toString());
                this.result = "AB";
            }








        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String result() {
        return result;
    }
}
