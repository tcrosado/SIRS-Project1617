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
import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Duarte on 20/11/2016.
 */
public class UDP {

    public String showBalance(String origIBAN) throws IOException {
        sendUDP('S', origIBAN, "", "");
        String balance = receiveUDP();
        return balance;
    }

    public void makeTransaction(String origIBAN, String destIBAN, String amount) throws IOException {
        sendUDP('T', origIBAN, destIBAN, amount);
    }


    private void sendUDP(char operation, String origIBAN, String destIBAN, String amount) throws IOException {
        /*
        UUID tid = UUID.randomUUID();
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();

        DatagramSocket clientSocket = new DatagramSocket();

        InetAddress IPAddress = InetAddress.getByName("localhost");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream daOp = new DataOutputStream(opBuffer);

        daOp.write(operation);
        daOp.writeBytes(origIBAN);
        daOp.writeBytes(destIBAN);
        daOp.writeDouble(amount);



        DatagramPacket sendPacket = new DatagramPacket(out.toByteArray(), out.size(), IPAddress, 5000);
        clientSocket.send(sendPacket);

        clientSocket.close();


        https://stackoverflow.com/questions/24519855/how-to-send-udp-packets-between-an-android-tablet-and-raspberry-pi

        InetAddress serverAddr = InetAddress.getByName(SERVERIP);
        DatagramSocket clientSocket = new DatagramSocket();
        byte[] sendData = new byte[1024];
        String sentence = message;
        sendData = sentence.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddr, SERVERPORT);
        clientSocket.send(sendPacket);
        clientSocket.close();

        */
    }

    private String receiveUDP() {
        /*
        byte[] receiveData1 = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData1, receiveData1.length);
        clientSocket.receive(receivePacket);
        clientSocket.close();

         */
        return "500.00";
    }

}
