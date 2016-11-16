package pt.ulisboa.tecnico.sirs.t07.service;

import pt.ulisboa.tecnico.sirs.t07.service.dto.OperationData;

import java.io.ByteArrayInputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by tiago on 12/11/2016.
 */
public class PacketParserService extends AbstractService {

    private DatagramPacket packet;
    private OperationData resultData;

    public  PacketParserService(DatagramPacket packet) throws Exception{
        setPacket(packet);
        resultData = null;
    }

    @Override
    void dispatch() {

        UUID tuid = this.getTId();
        Timestamp time = this.getTime();
        char operation = this.getOperation();


       switch (operation){

           case 'S':
               //TODO definir serviço de saldo
               System.out.println("Saldo");

               break;
           case 'T':
               //TODO definir serviço de tranferencia
               System.out.println("Transferencias");
               //this.resultData = new OperationData(tuid,time,new TransferService());
                break;
           case 'H':
               //TODO definir serviço de historico
               System.out.println("Histórico");
               break;
           default:
               //TODO enviar excecao
               System.out.println("Throw");
       }

    }


    private void setPacket(DatagramPacket packet) throws Exception{
        int MAX_LENGHT = 120;
        if(packet.getData().length>MAX_LENGHT){
            throw new Exception("tamanho grande"); //FIXME
        }
        this.packet = packet;
    }

    public OperationData result(){
        return resultData;
    }


    /**
     *          Outra funcoes
     *
     * **/


    private UUID getTId(){
        byte[] uidbyte1 = Arrays.copyOf(this.packet.getData(),8);
        byte[] uidbyte2 = Arrays.copyOfRange(this.packet.getData(),8,16);
        ByteBuffer bb = ByteBuffer.wrap(uidbyte1);
        long mostSignificant = bb.getLong();
        bb = ByteBuffer.wrap(uidbyte2);
        long leastSignificant = bb.getLong();
        return new UUID(mostSignificant,leastSignificant);
    }

    private Timestamp getTime(){
        byte[] timestampbyte = Arrays.copyOfRange(this.packet.getData(),16,24);
        ByteBuffer bb = ByteBuffer.wrap(timestampbyte);
        return new Timestamp(bb.getLong());
    }

    private byte[] getHash(){
        return Arrays.copyOfRange(this.packet.getData(),24,44);
    }

    private char getOperation(){
        byte[] opbyte = Arrays.copyOfRange(this.packet.getData(),44,45);
        return (char)(opbyte[0]);
    }

    private String getOriginIBAN(){
        byte[] ibanb = Arrays.copyOfRange(this.packet.getData(),45,70);
        return new String(ibanb);
    }

    private String getDestinationIBAN(){
        byte[] ibanb = Arrays.copyOfRange(this.packet.getData(),70,95);
        return new String(ibanb);
    }

    private double getTransferValue(){
        byte[] value = Arrays.copyOfRange(this.packet.getData(),95,103);
        ByteBuffer b = ByteBuffer.wrap(value);
        return b.getDouble();
    }
}

