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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.UUID;


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
            p = new PacketParserService(this.packet);
            p.execute();
        } catch (ErrorMessageException e) {
            try {
                handleError(e);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] data = new byte[1024];
        DatagramPacket receiveConfirmation = new DatagramPacket(data,data.length);
        try {
            OperationData opData = p.getResultData();
            opData.executeService();
            sendMessage(opData.getServiceResult().getBytes());
            logger.debug("sent Response");
            Arrays.fill( data, (byte) 0 );

        } catch (ErrorMessageException e) {
            try {
                handleError(e);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }



    private void handleError(ErrorMessageException e) throws NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();
        DataOutputStream daOp = new DataOutputStream(opBuffer);
        try {
            daOp.writeBytes(e.getMessage());
            sendMessage(opBuffer.toByteArray());

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }


    private void sendMessage(byte[] message) throws IOException, NoSuchAlgorithmException {
        UUID msgId = UUID.randomUUID();
        ByteArrayOutputStream buff = new ByteArrayOutputStream();
        DataOutputStream dBuff = new DataOutputStream(buff);
        dBuff.writeLong(msgId.getMostSignificantBits());
        dBuff.writeLong(msgId.getLeastSignificantBits());
        dBuff.write(message);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream join = new ByteArrayOutputStream();
        DataOutputStream daOp = new DataOutputStream(join);
        byte[] hash = digest.digest(buff.toByteArray());
        byte[] cappedHash = Arrays.copyOfRange(hash,8,24);
        logger.debug(new String(buff.toByteArray()));
        daOp.write(buff.toByteArray());
        daOp.write(cappedHash);

        this.conn.sendData(join.toByteArray(),this.packet.getAddress(),this.packet.getPort());
    }

}
