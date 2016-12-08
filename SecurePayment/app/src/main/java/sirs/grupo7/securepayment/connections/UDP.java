package sirs.grupo7.securepayment.connections;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sirs.grupo7.securepayment.encryption.AESFileEncryption;

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

    public String giveResponsetoChallenge(String cod1, String cod2, String cod3, String tid) throws IOException, NoSuchAlgorithmException {

        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('R');
        message.writeBytes(cod1);
        message.writeBytes(cod2);
        message.writeBytes(cod3);
        message.writeBytes(tid);
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());

        byte[] response = receiveUDP(clientSocket);


        // TODO WHAT DOES SERVER SEND TO ME AFTER RESPONSE to CHALLENGE???????????

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

        // TODO END WHAT DOES SERVER SEND TO ME AFTER RESPONSE to CHALLENGE???????????
    }

    public String showBalance(String origIBAN) throws IOException, NoSuchAlgorithmException {
        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('S');
        System.out.println("\nS\n");
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
        System.out.println("\nT\n");
        message.writeBytes("PT09876543210987654321098");
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

        AESFileEncryption aes = new AESFileEncryption();

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

        ByteArrayOutputStream messageStream = new ByteArrayOutputStream( );
        messageStream.write(cappedHash);
        messageStream.write(tempBuffer.toByteArray());

        byte[] p = {'A', 'A', 'A', 'A', 'A', 'A'};
        messageStream.write(p);

        ByteArrayOutputStream toSendBuffer = new ByteArrayOutputStream();

        DataOutputStream toSend = new DataOutputStream(toSendBuffer);

        toSend.writeBytes(PHONENUMBER);
        //toSend.write(cappedHash);
        byte[] cy;

        System.out.println("LEN = " + Arrays.toString(messageStream.toByteArray()));

        try {
            //toSend.write(aes.encrypt("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", messageStream.toByteArray()));
            cy = aes.encrypt("AAAAAAAAAAAAAAAAAAAA", messageStream.toByteArray());
            System.out.println("++++ " + new String(cy));
            toSend.write(cy);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
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
