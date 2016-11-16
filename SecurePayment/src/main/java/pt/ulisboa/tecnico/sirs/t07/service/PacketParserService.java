package pt.ulisboa.tecnico.sirs.t07.service;

import pt.ulisboa.tecnico.sirs.t07.service.dto.OperationData;

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
       System.out.println(this.packet.getData().length);
        byte[] uidbyte = Arrays.copyOf(this.packet.getData(),36);
        byte[] timestampbyte = Arrays.copyOfRange(this.packet.getData(),36,49);
        byte[] hashbyte = Arrays.copyOfRange(this.packet.getData(),49,62);
        byte[] opbyte = Arrays.copyOfRange(this.packet.getData(),62,63);

        UUID tuid = this.toUUID(uidbyte);
        Timestamp time = this.toTimeStamp(timestampbyte);
        String hash = hashbyte.toString();
        char operation = (char)(opbyte[0]);


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



    private UUID toUUID(byte[] uidbyte){
        String uids = new String(uidbyte);
        return UUID.fromString(uids);
    }


    private Timestamp toTimeStamp(byte[] timestamp){
        ByteBuffer bb = ByteBuffer.wrap(timestamp);
        return new Timestamp(bb.getLong());
    }
}

