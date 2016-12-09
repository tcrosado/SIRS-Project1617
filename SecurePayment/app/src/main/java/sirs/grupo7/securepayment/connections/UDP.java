package sirs.grupo7.securepayment.connections;

import android.content.Context;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sirs.grupo7.securepayment.encryption.AESFileEncryption;
import sirs.grupo7.securepayment.encryption.DHExchanger;
import sirs.grupo7.securepayment.readwrite.ReadWriteInfo;

/**
 * Created by Duarte on 20/11/2016.
 */
public class UDP {

    private String HOSTNAME;// = "192.168.43.219";
    //private String PHONENUMBER = "910000000";
    //private String PHONENUMBER = "911174628";

    private int PORT = 5000;
    private Context context;

    private int CODE_INDEX = 16;
    private int MONEY_INDEX = 17;
    private int INFO_INDEX = 17;

    private String code;

    public UDP(String hostname) {
        this.HOSTNAME = hostname;
    }

    public UDP(String hostname, Context context) {
        this.HOSTNAME = hostname;
        this.context = context;
    }

    public byte[] requestNewKey(String code) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException, InvalidKeyException {

        this.code = code;

        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('R');

        System.out.println(opBuffer);

        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());
        byte[] data = receiveUDP(clientSocket);
        SortedMap<Integer, byte[]> packets = new TreeMap<>();

        if (data[32] == 'R') {
            int packetLength = data[33];
            int packetNumber = data[34];
            byte[] messageRecv = Arrays.copyOfRange(data, 35, data.length);
            packets.put(packetNumber, messageRecv);
            int i = 0;
            int exit = 3;
            while (i < packetLength || exit == 0) {
                data = receiveUDP(clientSocket);
                if (data[32] == 'R') {
                    packetNumber = data[34];
                    messageRecv = Arrays.copyOfRange(data, 35, data.length);
                    packets.put(packetNumber, messageRecv);
                    i++;
                } else {
                    exit--;
                }
            }
        } else {
            return new byte[0];
        }

        String result = "";
        for ( Map.Entry<Integer,byte[]> e : packets.entrySet()){
            result += new String(e.getValue());
        }
        byte[] decode = Base64.decode(result, Base64.DEFAULT);

        DHExchanger dh = new DHExchanger(decode);
        byte[] dh_key = dh.getKey();

        byte[] key_pair = dh.getKeyPair();

        System.out.println("LENNNNNNN = " + key_pair.length);

        return dh_key;

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

        if (clientSocket == null) {
            throw new IOException();
        }

        byte[] response = receiveUDP(clientSocket);

        if (response.length == 0) {
            return "ERROR";
        }

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

        if (response.length == 0) {
            return "ERROR";
        }

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
        message.writeBytes(origIBAN);
        message.writeBytes(destIBAN);
        message.writeInt(Integer.parseInt(amount.replace(",", "")));
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());

        if (clientSocket == null) {
            throw new IOException();
        }

        byte[] response = receiveUDP(clientSocket);

        if (response.length == 0) {
            return "ERROR";
        }

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

        ByteArrayOutputStream toSendBuffer = new ByteArrayOutputStream();

        DataOutputStream toSend = new DataOutputStream(toSendBuffer);

        toSend.writeBytes(read(ReadWriteInfo.NUMBER));
        //toSend.write(cappedHash);
        byte[] cy;

        System.out.println("LEN = " + Arrays.toString(messageStream.toByteArray()));

        try {
            byte[] key = Base64.decode("vqJhHWzM6KtF4YUIZmbxng==", Base64.CRLF);
            //byte[] key = aes.decrypt(code, read(ReadWriteInfo.KEY).getBytes());
            //System.out.println("BEFORE");
            //System.out.println(key);
            //System.out.println("AFTER");
            cy = aes.encrypt(key, messageStream.toByteArray());
            System.out.println("++++ " + new String(cy));
            toSend.write(cy);
        } catch (InvalidKeySpecException | NoSuchPaddingException | InvalidParameterSpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        System.out.println("==== " + new String(toSendBuffer.toByteArray()));
        System.out.println("--------> " + Arrays.toString(toSendBuffer.toByteArray()));

        DatagramPacket sendPacket = new DatagramPacket(toSendBuffer.toByteArray(), toSendBuffer.size(), IPAddress, PORT);

        clientSocket.send(sendPacket);

        return clientSocket;
    }

    private byte[] receiveUDP(DatagramSocket clientSocket) throws IOException {
        AESFileEncryption aes = new AESFileEncryption();

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        clientSocket.close();
        System.out.println("RECEBIDO = " + new String(receivePacket.getData()));

        try {
            byte[] recv = aes.decrypt(Base64.decode("vqJhHWzM6KtF4YUIZmbxng==", Base64.NO_PADDING), receivePacket.getData());

            //byte[] recv = aes.decrypt(read(ReadWriteInfo.KEY), receivePacket.getData());
            byte[] recv_hash = Arrays.copyOfRange(recv, 0, 16);
            byte[] message = Arrays.copyOfRange(recv, 16, recv.length);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(message);
            byte[] cappedHash = Arrays.copyOfRange(hash, 8, 24);

            if (Arrays.equals(cappedHash, recv_hash)) {
                return recv;
            } else {
                return new byte[0];
            }

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | InvalidParameterSpecException | InvalidKeyException | InvalidKeySpecException | BadPaddingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private String read(String filename) {
        try {
            String message;
            FileInputStream fileInputStream = this.context.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((message = bufferedReader.readLine()) != null) {
                stringBuffer.append(message);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR";
    }
}
