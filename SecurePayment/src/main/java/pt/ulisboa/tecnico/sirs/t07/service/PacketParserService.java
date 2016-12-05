package pt.ulisboa.tecnico.sirs.t07.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InvalidHashException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.InvalidOperationException;
import pt.ulisboa.tecnico.sirs.t07.exceptions.MessageSizeExceededException;
import pt.ulisboa.tecnico.sirs.t07.service.dto.OperationData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private OperationData resultData;

    PacketParserService(DatagramPacket packet) throws Exception{
        setPacket(packet);
        resultData = null;
    }

    @Override
    void dispatch() throws ErrorMessageException {
        UUID tuid = this.getTId();
        Timestamp time = this.getTime();
        char operation = this.getOperation();

        logger.debug("Packet Parsed");
        logger.debug("Tid: {}",tuid);
        logger.debug("Time: {}",time);
        logger.debug("Op: {}",operation);

     //  veryfyIntegrity();

       switch (operation){

           case 'S':
               logger.debug("Operation: Balance");
               logger.debug("Account Iban : {}", this.getOriginIBAN());
               this.resultData = new OperationData(tuid,time,new BalanceCheckService(this.getOriginIBAN()));

               break;
           case 'T':
               logger.debug("Operation: Transfer");
               logger.debug("Origin Iban: {}",this.getOriginIBAN());
               logger.debug("Destination Iban: {}",this.getDestinationIBAN());
               logger.debug("Transfer Value: {}",this.getTransferValue());
               this.resultData = new OperationData(tuid,time,new TransferService(tuid,this.getOriginIBAN(),this.getDestinationIBAN(),this.getTransferValue()));
               break;
           case 'H':
               //TODO definir serviço de historico
               logger.debug("Operation: History");
               break;
           case 'R': //Request Matrix challenge - format -> result = "row-column"
        	   logger.debug("Operation: MatrixRequest");
        	   logger.debug("Requested Account : {}", this.getOriginIBAN());
        	   this.resultData = new OperationData(tuid,time, new GetMatrixRequestService(this.getOriginIBAN()));
        	   break;
           case 'C':
        	   logger.debug("Operation: Confirm Transaction with challenge response");
        	   logger.debug("Iban : {}", this.getOriginIBAN());
        	   logger.debug("Value 1 : {}", this.getMatrixResponseValues());
        	   logger.debug("Value 1 : {}", this.getMatrixResponseValues());
        	   logger.debug("Value 1 : {}", this.getMatrixResponseValues());
              // this.resultData = new OperationData(tuid,time,new ConfirmTransactionService(this.getConfirmationTid(),this.getMatrixResponseValue()));
        	   //FIXME this.resultData = new OperationData(tuid, time, new VerifyMatrixResponseService(getOriginIBAN(),getMatrixResponseValues()));
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

    private byte[] getHash(){
        return Arrays.copyOf(this.packet.getData(),16);
    }


    private UUID getTId(){
        byte[] uidbyte1 = Arrays.copyOfRange(this.packet.getData(),16,24);
        byte[] uidbyte2 = Arrays.copyOfRange(this.packet.getData(),24,32);
        ByteBuffer bb = ByteBuffer.wrap(uidbyte1);
        long mostSignificant = bb.getLong();
        bb = ByteBuffer.wrap(uidbyte2);
        long leastSignificant = bb.getLong();
        return new UUID(mostSignificant,leastSignificant);
    }

    private Timestamp getTime(){
        byte[] timestampbyte = Arrays.copyOfRange(this.packet.getData(),32,40);
        ByteBuffer bb = ByteBuffer.wrap(timestampbyte);
        return new Timestamp(bb.getLong());
    }

    private char getOperation(){
        byte[] opbyte = Arrays.copyOfRange(this.packet.getData(),40,41);
        return (char)(opbyte[0]);
    }

    private String getOriginIBAN(){
        byte[] ibanb = Arrays.copyOfRange(this.packet.getData(),41,66);
        return new String(ibanb);
    }

    private String getDestinationIBAN(){
        byte[] ibanb = Arrays.copyOfRange(this.packet.getData(),66,91);
        return new String(ibanb);
    }

    private double getTransferValue(){
        byte[] value = Arrays.copyOfRange(this.packet.getData(),91,99);
        ByteBuffer b = ByteBuffer.wrap(value);

        return b.getDouble();
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
        byte[] uidbyte1 = Arrays.copyOfRange(this.packet.getData(),41,49);
        byte[] uidbyte2 = Arrays.copyOfRange(this.packet.getData(),49,57);
        ByteBuffer bb = ByteBuffer.wrap(uidbyte1);
        long mostSignificant = bb.getLong();
        bb = ByteBuffer.wrap(uidbyte2);
        long leastSignificant = bb.getLong();
        return new UUID(mostSignificant,leastSignificant);
    }
    
    private AbstractList<Integer> getMatrixResponseValues(){
        Vector<Integer> vector = new Vector<Integer>();
        byte[] value1 = Arrays.copyOfRange(this.packet.getData(),57,61);
        byte[] value2 = Arrays.copyOfRange(this.packet.getData(),61,65);
        byte[] value3 = Arrays.copyOfRange(this.packet.getData(),65,69);

        vector.add(new Integer(new String(value1)));
        vector.add(new Integer(new String(value2)));
        vector.add(new Integer(new String(value3)));
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
        byte[] data = Arrays.copyOfRange(this.packet.getData(),16,99);

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

