package pt.ulisboa.tecnico.sirs.t07.data;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Vector;

/**
 * Created by trosado on 09/11/16.
 */
public class CustomerData extends AbstractData {


    public Vector<String> getIBANFromPhone(String phone){

        Vector<String> result = new Vector<String>();
        try {
            PreparedStatement stmt = conn.prepareStatement("SELECT iban FROM phone_iban WHERE phoneNumber = ?");
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
            PreparedStatement stmt = conn.prepareStatement("SELECT phoneNumber FROM phone_iban WHERE iban = ?");
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
            PreparedStatement stmt = conn.prepareStatement("SELECT iban FROM phone_iban WHERE iban = ?");
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

    public byte[] getIV(String phoneNumber) {
        byte[] iv = new byte[0];
        try {
            String ivEnc = "";
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE phoneNumber=?");
            stmt.setString(1, phoneNumber);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                ivEnc = rs.getString("iv");
                iv = Base64.getDecoder().decode(ivEnc);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return iv;
    }
        public byte[] getBankCode(String phoneNumber){
        byte[] key = new byte[0];
        try {
            String result = "";
            String ivEnc = "";
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers WHERE phoneNumber=?");
            stmt.setString(1, phoneNumber);

            ResultSet rs = stmt.executeQuery();


            while (rs.next()) {
                result = rs.getString("bankCode");
                ivEnc = rs.getString("iv");
            }

            byte[] iv = Base64.getDecoder().decode(ivEnc);
            byte[] keyPhone = phoneNumber.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            keyPhone = sha.digest(keyPhone);
            keyPhone = Arrays.copyOf(keyPhone, 16);

            SecretKey secret = new SecretKeySpec(keyPhone, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
            key = cipher.doFinal(Base64.getDecoder().decode(result.getBytes()));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return key; //FIXME
    }
}
