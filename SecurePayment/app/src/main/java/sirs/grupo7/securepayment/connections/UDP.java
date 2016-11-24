package sirs.grupo7.securepayment.connections;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Duarte on 20/11/2016.
 */
public class UDP {
    
    private String HOSTNAME;
    private int PORT;
    
    public UDP(String hostname, int port) {
        // if Optional is supported use it
        this.HOSTNAME = hostname;
        this.PORT = port;
    }

    public String showBalance(String origIBAN) throws IOException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('S');
        message.writeBytes(origIBAN);
        sendUDP(opBuffer.toByteArray());
        return receiveUDP();
    }

    public String makeTransaction(String origIBAN, String destIBAN, String amount) throws IOException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('T');
        message.writeBytes(origIBAN);
        message.writeBytes(destIBAN);
        message.writeBytes(amount);
        sendUDP(opBuffer.toByteArray());
        return receiveUDP();
    }
    
    public String showHistory(String origIBAN) throws IOException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('H');
        message.writeBytes(origIBAN);
        sendUDP(opBuffer.toByteArray());
        return receiveUDP();
    }

    private void sendUDP(byte[] message) throws IOException {

        UUID tid = UUID.randomUUID();
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();

        DatagramSocket clientSocket = new DatagramSocket();

        InetAddress IPAddress = InetAddress.getByName(HOSTNAME);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();

            DataOutputStream temp = new DataOutputStream(tempBuffer);

            temp.writeLong(tid.getMostSignificantBits());
            temp.writeLong(tid.getLeastSignificantBits());
            temp.writeLong(time);
            temp.write(message);

            byte[] hash = digest.digest(tempBuffer.toByteArray());
            byte[] cappedHash = Arrays.copyOfRange(hash,8,24);

            ByteArrayOutputStream toSendBuffer = new ByteArrayOutputStream();

            DataOutputStream toSend = new DataOutputStream(tempBuffer);

            toSend.write(cappedHash);
            toSend.write(tempBuffer.toByteArray());


            DatagramPacket sendPacket = new DatagramPacket(toSendBuffer.toByteArray(), toSendBuffer.size(), IPAddress, PORT);
            clientSocket.send(sendPacket);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        clientSocket.close();

    }

    private String receiveUDP() throws IOException {

        DatagramSocket clientSocket = new DatagramSocket();
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);
        clientSocket.close();

        return new String(receivePacket.getData());
    }

}
