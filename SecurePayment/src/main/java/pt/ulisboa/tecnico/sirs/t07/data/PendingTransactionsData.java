package pt.ulisboa.tecnico.sirs.t07.data;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by trosado on 05/12/16.
 */
public class PendingTransactionsData extends AbstractData {

    public void addPendingTransaction(String tid, String originIban,String destIban, double value,String row,int col) throws SQLException {

        PreparedStatement recordTransaction = conn.prepareStatement("INSERT INTO pendingTransactions VALUES (?,CURRENT_TIMESTAMP,?,?,?,?,?);");

        recordTransaction.setString(1,tid);
        recordTransaction.setString(2,originIban);
        recordTransaction.setString(3,destIban);
        recordTransaction.setDouble(4,value);
        recordTransaction.setString(5,row);
        recordTransaction.setInt(6,col);
        recordTransaction.execute();

    }
}
