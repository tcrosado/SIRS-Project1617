package pt.ulisboa.tecnico.sirs.t07.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InsufficientFundsException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.MaxWithdrawLimitException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by trosado on 17/11/16.
 */
public class TransferHistoryData extends AbstractData {

    public void doTransaction(String tid, String originIban,String destIban, int value) throws ErrorMessageException, SQLException {
        AccountData accountDB = new AccountData();
        int balance;
            conn.setAutoCommit(false);
            PreparedStatement recordTransaction = conn.prepareStatement("INSERT INTO transactionHistory VALUES (?,CURRENT_TIMESTAMP,?,?,?);");

            recordTransaction.setString(1,tid);
            recordTransaction.setString(2,originIban);
            recordTransaction.setString(3,destIban);
            recordTransaction.setDouble(4,value);
            recordTransaction.execute();

            PreparedStatement updateBalanceOrigin = conn.prepareStatement("UPDATE accounts SET balance=? WHERE iban= ?;");
            balance = accountDB.getBalanceFromIBAN(originIban).firstElement();
            if(balance<value){
                recordTransaction.cancel();
                conn.setAutoCommit(true);
                throw new InsufficientFundsException(originIban);
            }

            /*FIXME if(getDayTransferWithdrawValue(originIban)>=400){
                recordTransaction.cancel();
                conn.setAutoCommit(true);
                throw new MaxWithdrawLimitException(originIban);
            }*/

            updateBalanceOrigin.setDouble(1,balance-value);
            updateBalanceOrigin.setString(2,originIban);
            updateBalanceOrigin.execute();

            PreparedStatement updateBalanceDest = conn.prepareStatement("UPDATE accounts SET balance=? WHERE iban= ?;");
            balance = accountDB.getBalanceFromIBAN(destIban).firstElement();

            updateBalanceDest.setDouble(1,balance+value);
            updateBalanceDest.setString(2,destIban);
            updateBalanceDest.execute();
            conn.commit();
            conn.setAutoCommit(true);

    }

    public Vector<TransferHistory> getLastTransactionFromIban(String iban,Optional<Integer> nr) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM transactionHistory WHERE originIban= ? OR destIban= ?;") ;
        stmt.setString(1,iban);
        stmt.setString(2,iban);

        ResultSet rs = stmt.executeQuery();
        Vector<TransferHistory> result = new Vector<TransferHistory>();

        while (rs.next() || nr.orElse(200)<result.size()){
            UUID tid = UUID.fromString(rs.getString("tid"));
            Timestamp time = rs.getTimestamp("time");
            String originIban = rs.getString("originIban");
            String destIban = rs.getString("destIban");
            int value = rs.getInt("value");

            TransferHistory transfer = new TransferHistory(tid,time,originIban,destIban,value);
            result.add(transfer);
        }

        return result;
    }

    public int getDayTransferWithdrawValue(String ibanOrigin) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT SUM(value) FROM transactionHistory WHERE originIban= ? AND time > CURRENT_DATE;");
        stmt.setString(1,ibanOrigin);

        ResultSet rs = stmt.executeQuery();
        Vector<TransferHistory> result = new Vector<TransferHistory>();
        int value = 0;
        while (rs.next()){
            value = rs.getInt("SUM(value)");
        }

        return value;
    }
}
