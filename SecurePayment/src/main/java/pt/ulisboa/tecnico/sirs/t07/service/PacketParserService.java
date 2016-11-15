package pt.ulisboa.tecnico.sirs.t07.service;

import pt.ulisboa.tecnico.sirs.t07.service.dto.OperationData;

import java.net.DatagramPacket;
import java.util.Arrays;

/**
 * Created by tiago on 12/11/2016.
 */
public class PacketParserService extends AbstractService {

    private DatagramPacket packet;
    private OperationData resultData;

    public  PacketParserService(DatagramPacket packet) throws Exception{
        setPacket(packet);
        resultData = null;
        System.out.print("yep");
    }

    @Override
    void dispatch() {
       System.out.println(this.packet.getData().length);
        byte[] uid = Arrays.copyOf(this.packet.getData(),16);
        System.out.println(Arrays.toString(uid));
       byte[] timestamp = Arrays.copyOfRange(this.packet.getData(),16,32);
       byte[] hash = Arrays.copyOfRange(this.packet.getData(),32,48);
       byte[] opbyte = Arrays.copyOfRange(this.packet.getData(),48,64);
        System.out.print(Arrays.toString(opbyte));
        System.out.print("got it");
        char operation = (char)(((opbyte[0]&0x00FF)<<8) + (opbyte[1]&0x00FF));
        System.out.print("a:"+operation);
       switch (operation){

           case 'S':
               //TODO definir serviço de saldo
               System.out.println("Saldo");
               break;
           case 'T':
               //TODO definir serviço de tranferencia
               System.out.println("Transferencias");
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


}

