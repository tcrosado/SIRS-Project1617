package pt.ulisboa.tecnico.sirs.t07.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

/**
 * Created by trosado on 05/12/16.
 */
public class PendingTransactionsData extends AbstractData {

    public void addPendingTransaction(String tid, String originIban,String destIban, double value,Vector<MatrixPosition> positions) throws SQLException {

        PreparedStatement recordTransaction = conn.prepareStatement("INSERT INTO pendingTransactions VALUES (?,CURRENT_TIMESTAMP,?,?,?);");

        recordTransaction.setString(1,tid);
        recordTransaction.setString(2,originIban);
        recordTransaction.setString(3,destIban);
        recordTransaction.setDouble(4,value);
        recordTransaction.execute();
        int i = 0;
        for( MatrixPosition position : positions){
            PreparedStatement recordPosition = conn.prepareStatement("INSERT INTO pendingChallenges VALUES (?,?,?,?,?);");

            recordPosition.setString(1,tid);
            recordPosition.setInt(2,i++);
            recordPosition.setString(3,position.getRow());
            recordPosition.setInt(4,position.getCol());
            recordPosition.setInt(5,position.getPos());
            recordPosition.execute();
        }

    }
}
