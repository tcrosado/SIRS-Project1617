package sirs.grupo7.securepayment.connections;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

/**
 * Created by Duarte on 20/11/2016.
 */
public class UDP {

    private String HOSTNAME = "192.168.43.219";
    private String PHONENUMBER = "910000000";
    private int PORT = 5000;

    private int CODE_INDEX = 16;
    private int MONEY_INDEX = 17;
    private int INFO_INDEX = 17;


    public UDP() {
    }

    public void requestNewKey() throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('R');
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());
        byte[] data = receiveUDP(clientSocket);

        // TODO Request New Key
    }

    public String showBalance(String origIBAN) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('S');
        message.writeBytes(origIBAN);
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());

        byte[] response = receiveUDP(clientSocket);

        if (response[CODE_INDEX] == 'B') {
            String money = "";
            int i = MONEY_INDEX;
            while (response[i] != '$') {
                money += (char) response[i];
                i++;
            }
            if (i > 2) {
                return money.substring(0, money.length() - 2) + "," + money.substring(money.length() - 2);
            } if (i > 1) {
                return "0," + money;
            } else {
                return "0,0" + money;
            }
        } else {
            return "ERROR";
        }
    }

    public String makeTransaction(String origIBAN, String destIBAN, String amount) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('T');
        message.writeBytes("PT12345678901234567890123");
        message.writeBytes(destIBAN);
        message.writeInt(Integer.parseInt(amount.replace(",", "")));
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());

        byte[] response = receiveUDP(clientSocket);

        System.out.println("CODE = " + response[CODE_INDEX]);
        if (response[CODE_INDEX] == 'T') {
            System.out.println("INFO = " + response[INFO_INDEX]);
            if (response[INFO_INDEX] == 'P') {
                return new String(Arrays.copyOfRange(response, INFO_INDEX, INFO_INDEX + 46));
            } else {
                return "" + (char) response[INFO_INDEX];
            }
        } else {
            return "ERROR";
        }
    }
    
    public String showHistory(String origIBAN) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('H');
        message.writeBytes(origIBAN);
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());
        return "ola";
        //return receiveUDP(clientSocket);
    }

    private DatagramSocket sendUDP(byte[] message) throws IOException, NoSuchAlgorithmException {

        UUID tid = UUID.randomUUID();
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress IPAddress = InetAddress.getByName(HOSTNAME);
        System.out.println("TID " + tid);
        System.out.println(tid.getMostSignificantBits());
        System.out.println(tid.getLeastSignificantBits());

        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        ByteArrayOutputStream tempBuffer = new ByteArrayOutputStream();

        DataOutputStream temp = new DataOutputStream(tempBuffer);

        temp.writeLong(tid.getMostSignificantBits());
        temp.writeLong(tid.getLeastSignificantBits());
        temp.write(message);

        System.out.println(Arrays.toString(tempBuffer.toByteArray()));
        byte[] hash = digest.digest(tempBuffer.toByteArray());
        byte[] cappedHash = Arrays.copyOfRange(hash,8,24);

        ByteArrayOutputStream toSendBuffer = new ByteArrayOutputStream();

        DataOutputStream toSend = new DataOutputStream(toSendBuffer);

        toSend.writeBytes(PHONENUMBER);
        toSend.write(cappedHash);
        toSend.write(tempBuffer.toByteArray());
        System.out.println("==== " + new String(toSendBuffer.toByteArray()));
        DatagramPacket sendPacket = new DatagramPacket(toSendBuffer.toByteArray(), toSendBuffer.size(), IPAddress, PORT);

        clientSocket.send(sendPacket);

        return clientSocket;
    }

    private byte[] receiveUDP(DatagramSocket clientSocket) throws IOException {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        clientSocket.close();
        System.out.println("RECEBIDO = " + new String(receivePacket.getData()));

        return receivePacket.getData();
    }
}
