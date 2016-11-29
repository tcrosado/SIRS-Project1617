package pt.ulisboa.tecnico.sirs.t07.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.service.dto.OperationData;
import pt.ulisboa.tecnico.sirs.t07.utils.UDPConnection;
import sun.security.x509.IPAddressName;

import javax.xml.crypto.Data;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.PriorityQueue;


/**
 * Created by tiago on 12/11/2016.
 */
public class UDPEstablishService extends AbstractService implements Runnable{

    //private AbstractQueue<DatagramPacket> packets;
    private final Logger logger = LoggerFactory.getLogger(UDPEstablishService.class);
    private Integer timeout;
    private DatagramPacket packet;
    private UDPConnection conn;


    public UDPEstablishService(UDPConnection conn, DatagramPacket packet, Integer timeout){
        //this.packets = new PriorityQueue<DatagramPacket>();
        this.packet = packet;
        this.timeout = timeout;
        this.conn = conn;

    }

    @Override
    void dispatch() {
        run();
    }

    @Override
    public void run() {
        /*
        * 1 - receber pedido e enviar confirmaçao
        * 2 - receber confirmacao e fazer operacao
        * */

        PacketParserService p = null;
        try {
            logger.debug(new String(this.packet.getData()));
            p = new PacketParserService(this.packet);
            p.execute();
        } catch (ErrorMessageException e) {
           handleError(e);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
/*
        try {
            sendMessage(this.packet.getData()); //TODO aqui deve enviar-se tambem o challenge response
        } catch (IOException e) {
            e.printStackTrace();
        }
*/
        byte[] data = new byte[1024];
        DatagramPacket receiveConfirmation = new DatagramPacket(data,data.length);
        try {
            OperationData opData = p.getResultData();
            opData.executeService();
            logger.debug(opData.getServiceResult());

            sendMessage(opData.getServiceResult().getBytes());
            logger.debug("sent");
       /*     this.socket.setSoTimeout(this.timeout);
            this.socket.receive(receiveConfirmation);
            this.socket.setSoTimeout(0);
            if(Arrays.equals(receiveConfirmation.getData(),sendPacket.getData())){
                //FIXME tratar de verificar confirmacao
                p.result();
                logger.debug("Operation executed");
            }else{
                logger.debug("Operation aborted");
            }*/
            Arrays.fill( data, (byte) 0 );

        } catch (ErrorMessageException e) {
            handleError(e);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    private void handleError(ErrorMessageException e){
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();
        DataOutputStream daOp = new DataOutputStream(opBuffer);
        try {
            daOp.writeBytes(e.getMessage());
            sendMessage(opBuffer.toByteArray());

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    private void sendMessage(byte[] message) throws IOException {
        DatagramPacket packet = new DatagramPacket(message,message.length,this.packet.getAddress(),this.packet.getPort());
        this.conn.sendData(message,this.packet.getAddress(),this.packet.getPort());
    }

}
