package sirs.grupo7.securepayment.connections;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Duarte on 20/11/2016.
 */
public class UDP {

    private String HOSTNAME = "10.42.0.69";
    private int PORT = 5000;
    
    public UDP() {
    }

    public String showBalance(String origIBAN) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('S');
        message.writeBytes(origIBAN);
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());
        return receiveUDP(clientSocket);
    }

    public String makeTransaction(String origIBAN, String destIBAN, String amount) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('T');
        message.writeBytes(origIBAN);
        message.writeBytes(destIBAN);
        message.writeDouble(Double.parseDouble(amount.replace(",", ".")));
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());
        return receiveUDP(clientSocket);

    }
    
    public String showHistory(String origIBAN) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('H');
        message.writeBytes(origIBAN);
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());
        return receiveUDP(clientSocket);
    }

    private DatagramSocket sendUDP(byte[] message) throws IOException, NoSuchAlgorithmException {

        UUID tid = UUID.randomUUID();
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(HOSTNAME);
        System.out.println("TID " + tid);
        System.out.println(tid.getMostSignificantBits());
        System.out.println(tid.getLeastSignificantBits());
        System.out.println(calendar.getTimeInMillis());

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();

        DataOutputStream temp = new DataOutputStream(tempBuffer);

        temp.writeLong(tid.getMostSignificantBits());
        temp.writeLong(tid.getLeastSignificantBits());
        temp.writeLong(time);
        temp.write(message);

        System.out.println(Arrays.toString(tempBuffer.toByteArray()));
        byte[] hash = digest.digest(tempBuffer.toByteArray());
        byte[] cappedHash = Arrays.copyOfRange(hash,8,24);

        ByteArrayOutputStream toSendBuffer = new ByteArrayOutputStream();

        DataOutputStream toSend = new DataOutputStream(toSendBuffer);

        toSend.write(cappedHash);
        toSend.write(tempBuffer.toByteArray());
        System.out.println(Arrays.toString(toSendBuffer.toByteArray()));
        DatagramPacket sendPacket = new DatagramPacket(toSendBuffer.toByteArray(), toSendBuffer.size(), IPAddress, PORT);

        clientSocket.send(sendPacket);

        return clientSocket;
    }

    private String receiveUDP(DatagramSocket clientSocket) throws IOException {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        clientSocket.close();

        return new String(receivePacket.getData());
    }

}
