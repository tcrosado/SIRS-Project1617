	package pt.ulisboa.tecnico.sirs.t07.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.sirs.t07.data.CustomerData;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InvalidHashException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InvalidOperationException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.MessageSizeExceededException;
import pt.ulisboa.tecnico.sirs.t07.service.dto.OperationData;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by tiago on 12/11/2016.
 */
class PacketParserService extends OperationService {

    private final Logger logger = LoggerFactory.getLogger(PacketParserService.class);
    private DatagramPacket packet;
    private byte[] decriptedMessage;
    private OperationData resultData;

    PacketParserService(DatagramPacket packet) throws Exception{
        setPacket(packet);
        resultData = null;
        decriptedMessage = null;
    }

    @Override
    void dispatch() throws ErrorMessageException {


        CustomerData cd = new CustomerData();

        String code = cd.getBankCode(this.getPhoneNumber());
        byte [] iv = "1234567812345678".getBytes();
        SecretKeyFactory factory = null;
        try {
            byte[] key = code.getBytes("UTF-8");
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);

            SecretKey secret = new SecretKeySpec(key, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret,new IvParameterSpec(iv));
            logger.debug(Arrays.toString(cipher.doFinal(Arrays.copyOfRange(this.packet.getData(),9,105))));
            decriptedMessage = cipher.doFinal(Arrays.copyOfRange(this.packet.getData(),9,105));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        UUID tuid = this.getTId();
        char operation = this.getOperation();

        logger.debug("Packet Parsed");
        logger.debug("Phone Number: {}",this.getPhoneNumber());
        logger.debug("Tid: {}",tuid);
        logger.debug("Op: {}",operation);
        //  veryfyIntegrity();

       switch (operation){

           case 'S':
               logger.debug("Operation: Balance");
               logger.debug("Account Iban : {}", this.getOriginIBAN());
               this.resultData = new OperationData(tuid,new BalanceCheckService(this.getOriginIBAN()));

               break;
           case 'T':
               logger.debug("Operation: Transfer");
               logger.debug("Origin Iban: {}",this.getOriginIBAN());
               logger.debug("Destination Iban: {}",this.getDestinationIBAN());
               logger.debug("Transfer Value: {}",this.getTransferValue());
               this.resultData = new OperationData(tuid,new TransferService(tuid,this.getOriginIBAN(),this.getDestinationIBAN(),this.getTransferValue()));
               break;
           case 'H':
               //TODO definir serviço de historico
               logger.debug("Operation: History");
               break;
           case 'C':
        	   logger.debug("Operation: Confirm Transaction with challenge response");
        	   logger.debug("tid : {}", this.getConfirmationTid());
        	   logger.debug("Value 1 : {}", this.getMatrixResponseValues().get(0));
        	   logger.debug("Value 2 : {}", this.getMatrixResponseValues().get(1));
        	   logger.debug("Value 3 : {}", this.getMatrixResponseValues().get(2));
               this.resultData = new OperationData(tuid,new ConfirmTransactionService(this.getConfirmationTid(),this.getMatrixResponseValues()));
        	   break;
           default:
              throw new InvalidOperationException();
       }

    }

    @Override
    public String result() {
        return "Packet Parser doesn't implement result method";
    }

    OperationData getResultData(){
        return resultData;
    }

    private void setPacket(DatagramPacket packet) throws Exception{
        int MAX_LENGHT = 120;
        if(packet.getLength()>MAX_LENGHT){
            throw new MessageSizeExceededException();
        }
        this.packet = packet;
    }


    /**
     *          Outra funcoes
     *
     * **/

    private String getPhoneNumber(){ return new String(Arrays.copyOf(this.packet.getData(),9));}

    private byte[] getHash(){
        return Arrays.copyOf(decriptedMessage,16);
    }


    private UUID getTId(){
        final int UUIDPOSITION = 16;
        byte[] uidbyte1 = Arrays.copyOfRange(decriptedMessage,UUIDPOSITION,UUIDPOSITION+8);
        byte[] uidbyte2 = Arrays.copyOfRange(decriptedMessage,UUIDPOSITION+8,UUIDPOSITION+16);
        ByteBuffer bb = ByteBuffer.wrap(uidbyte1);
        long mostSignificant = bb.getLong();
        bb = ByteBuffer.wrap(uidbyte2);
        long leastSignificant = bb.getLong();
        return new UUID(mostSignificant,leastSignificant);
    }

    private char getOperation(){
        final int OPPOSITION = 32;
        byte[] opbyte = Arrays.copyOfRange(decriptedMessage,OPPOSITION,OPPOSITION+1);
        return (char)(opbyte[0]);
    }

    private String getOriginIBAN(){
        final int OIBANPOS = 33;
        byte[] ibanb = Arrays.copyOfRange(decriptedMessage,OIBANPOS,OIBANPOS+25);
        return new String(ibanb);
    }

    private String getDestinationIBAN(){
        final int DIBANPOS = 58;
        byte[] ibanb = Arrays.copyOfRange(decriptedMessage,DIBANPOS,DIBANPOS+25);
        return new String(ibanb);
    }

    private int getTransferValue(){
        final int VALUEPOS = 83;
        byte[] value = Arrays.copyOfRange(decriptedMessage,VALUEPOS,VALUEPOS+4);
        ByteBuffer b = ByteBuffer.wrap(value);
        return b.getInt();
    }
    
    // Joao - Assumindo que o pacote quando so tem um NIB quando � response da matriz e vem row|column|value para validacao
    private String getMatrixResponseRow(){
    	byte[] row = Arrays.copyOfRange(this.packet.getData(),66,67);
        return new String(row);
    }
    
    private int getMatrixResponseColumn(){
    	byte[] column = Arrays.copyOfRange(this.packet.getData(),67,68);
        return new Integer(new String(column));
    }


    private UUID getConfirmationTid(){
        final int CTIDPOS = 33;
        byte[] uidbyte1 = Arrays.copyOfRange(decriptedMessage,CTIDPOS,CTIDPOS+8);
        byte[] uidbyte2 = Arrays.copyOfRange(decriptedMessage,CTIDPOS+8,CTIDPOS+16);
        ByteBuffer bb = ByteBuffer.wrap(uidbyte1);
        long mostSignificant = bb.getLong();
        bb = ByteBuffer.wrap(uidbyte2);
        long leastSignificant = bb.getLong();
        return new UUID(mostSignificant,leastSignificant);
    }
    
    private AbstractList<Integer> getMatrixResponseValues(){
        final int RESPOS = 49;
        Vector<Integer> vector = new Vector<Integer>();
        byte[] value1 = Arrays.copyOfRange(decriptedMessage,RESPOS,RESPOS+4);
        byte[] value2 = Arrays.copyOfRange(decriptedMessage,RESPOS+4,RESPOS+8);
        byte[] value3 = Arrays.copyOfRange(decriptedMessage,RESPOS+8,RESPOS+12);

        ByteBuffer b = ByteBuffer.wrap(value1);
        vector.add(b.getInt());

        b = ByteBuffer.wrap(value2);
        vector.add(b.getInt());

        b = ByteBuffer.wrap(value3);
        vector.add(b.getInt());
        return vector;
    }
    
    //TODO Valida isto por favor Tiago
    //--------------------------------
    private void veryfyIntegrity() throws InvalidHashException {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] data = Arrays.copyOfRange(this.packet.getData(),25,100);

        try {
            out.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] calculatedHash = digest.digest(out.toByteArray());
        byte[] cappedHash = Arrays.copyOfRange(calculatedHash,8,24);
        byte[] hash = this.getHash();

        if(!Arrays.equals(cappedHash,hash))
            throw new InvalidHashException(this.getTId());

    }
}

