package pt.ulisboa.tecnico.sirs.t07.data;

import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public String getIbanFromTid(String tid) throws SQLException {

        PreparedStatement transaction = conn.prepareStatement("SELECT * FROM pendingTransactions WHERE tid=?");

        transaction.setString(1,tid);
        ResultSet set = transaction.executeQuery();
        set.next();

        return set.getString("originIban");
    }

    public MatrixPosition getMatrixCoordenates(String tid,int order) throws SQLException {

        PreparedStatement coordenates = conn.prepareStatement("SELECT * FROM bank.pendingChallenges WHERE tid = ? AND `order` = ?");
        coordenates.setString(1,tid);
        coordenates.setInt(2,order);

        ResultSet set = coordenates.executeQuery();

        set.next();
        return new MatrixPosition(set.getString("row"),set.getInt("column"),set.getInt("position"));
    }

    public Boolean confirmTransaction(String tid) throws SQLException, ErrorMessageException {

        TransferHistoryData data = new TransferHistoryData();

        PreparedStatement pendingTransfer = conn.prepareStatement("SELECT * FROM bank.pendingtransactions WHERE tid = ?");
        pendingTransfer.setString(1,tid);

        ResultSet set = pendingTransfer.executeQuery();

        while(set.next()){
            String recordTid = set.getString("tid");
            String originIban = set.getString("originIban");
            String destinationIban = set.getString("destIban");
            Double value = set.getDouble("value");

            data.doTransaction(recordTid,originIban,destinationIban,value);
        }

        PreparedStatement deleteCompletedTransfers = conn.prepareStatement("DELETE FROM bank.pendingtransactions WHERE tid = ?");
        deleteCompletedTransfers.setString(1,tid);

        return deleteCompletedTransfers.execute();
    }

    public Boolean abortTransaction(String tid) throws SQLException {
        PreparedStatement deleteCompletedTransfers = conn.prepareStatement("DELETE FROM bank.pendingtransactions WHERE tid = ?");
        deleteCompletedTransfers.setString(1,tid);

        return deleteCompletedTransfers.execute();
    }
}
