package pt.ulisboa.tecnico.sirs.t07.data;
import java.sql.*;
import java.util.Vector;

/**
 * Created by trosado on 09/11/16.
 */
public class CustomerData extends AbstractData {


    public Vector<String> getIBANFromPhone(String phone){

        Vector<String> result = new Vector<String>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT iban FROM customers WHERE phoneNumber = ?");
            stmt.setString(1,phone);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()){
                result.add(rs.getString("iban"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Vector<String> getPhoneFromIBAN(String iban){

        Vector<String> result = new Vector<String>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT phoneNumber FROM customers WHERE iban = ?");
            stmt.setString(1,iban);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()){
                result.add(rs.getString("phoneNumber"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean ibanExists(String iban){
        Vector<String> result = new Vector<String>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT iban FROM customers WHERE iban = ?");
            stmt.setString(1,iban);
            ResultSet rs = stmt.executeQuery();


            while (rs.next()){
                result.add(rs.getString("iban"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return !(result.isEmpty());
    }

    public String getBankCode(String phoneNumber){
        String result = "";
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE phoneNumber=?");
            stmt.setString(1,phoneNumber);

            ResultSet rs = stmt.executeQuery();


            while(rs.next()){
                result=rs.getString("bankCode");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result; //FIXME
    }
}
