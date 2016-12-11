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
    private int CODE_BALANCE_INDEX = 32;
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

    public String giveResponsetoChallenge(String cod1, String cod2, String cod3, String tid, String code) throws IOException, NoSuchAlgorithmException {
        this.code = code;

        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('C');
        message.writeLong(UUID.fromString(tid).getMostSignificantBits());
        message.writeLong(UUID.fromString(tid).getLeastSignificantBits());
        message.writeBytes(cod1);
        message.writeBytes(cod2);
        message.writeBytes(cod3);
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());

        if (clientSocket == null) {
            throw new IOException();
        }

        byte[] response = receiveUDP(clientSocket);

        System.out.println("NOT LAST");
        System.out.println(Arrays.toString(response));
        System.out.println("LAST");

        if (response.length == 0) {
            return "ERROR";
        }

        // TODO WHAT DOES SERVER SEND TO ME AFTER RESPONSE to CHALLENGE???????????

        if (response[CODE_BALANCE_INDEX] == 'T') {
            System.out.println("INFO = " + response[CODE_BALANCE_INDEX]);
            return new String(Arrays.copyOfRange(response, CODE_BALANCE_INDEX + 1, CODE_BALANCE_INDEX + 2));
        } else {
            return "ERROR";
        }

        // TODO END WHAT DOES SERVER SEND TO ME AFTER RESPONSE to CHALLENGE???????????
    }

    public String showBalance(String origIBAN, String code) throws IOException, NoSuchAlgorithmException {
        this.code = code;

        ByteArrayOutputStream opBuffer = new ByteArrayOutputStream();

        DataOutputStream message = new DataOutputStream(opBuffer);
        message.write('S');
        System.out.println("\nS\n");
        message.writeBytes(origIBAN);
        DatagramSocket clientSocket = sendUDP(opBuffer.toByteArray());

        byte[] response = receiveUDP(clientSocket);

        if (response.length == 0) {
            return "ERROR LEN 0";
        }

        if (response[CODE_BALANCE_INDEX] == 'B') {
            String money = "";
            int i = CODE_BALANCE_INDEX + 1;
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
            return "ERROR B NOT FOUND";
        }
    }

    public String makeTransaction(String origIBAN, String destIBAN, String amount, String code) throws IOException, NoSuchAlgorithmException {
        this.code = code;

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

        System.out.println("CODE = " + response[CODE_BALANCE_INDEX]);
        if (response[CODE_BALANCE_INDEX] == 'T') {
            System.out.println("INFO = " + response[CODE_BALANCE_INDEX]);
            if (response[CODE_BALANCE_INDEX + 1] == 'P') {
                System.out.println(new String (Arrays.copyOfRange(response, CODE_BALANCE_INDEX + 1, CODE_BALANCE_INDEX + 20)));
                //System.out.println(Arrays.toString(Arrays.copyOfRange(response, CODE_BALANCE_INDEX + 1, CODE_BALANCE_INDEX + 20)));
                return new String (Arrays.copyOfRange(response, CODE_BALANCE_INDEX + 1, response.length));
            } else {
                return "" + (char) response[CODE_BALANCE_INDEX + 1];
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

        AESFileEncryption aes = new AESFileEncryption(this.context);

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
            //byte[] key = Base64.decode("vqJhHWzM6KtF4YUIZmbxng==",Base64.NO_WRAP);


            //byte[] key = {118, 113, 74, 104, 72, 87, 122, 77, 54, 75, 116, 70, 52, 89, 85, 73, 90, 109, 98, 120, 110, 103, 61, 61};
            //System.out.println("Print Key decoded  ---- " + Arrays.toString(key));
            byte[] key = aes.decrypt(makeHash(code), Base64.decode(read(ReadWriteInfo.KEY), Base64.NO_WRAP));
            System.out.println("BEFORE");
            System.out.println(Arrays.toString(key));
            System.out.println("AFTER");
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

    private byte[] makeHash(String code) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(code.getBytes());
    }

    private byte[] makeHash(byte[] code) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(code);
    }

    private byte[] receiveUDP(DatagramSocket clientSocket) throws IOException {
        AESFileEncryption aes = new AESFileEncryption(this.context);

        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        clientSocket.receive(receivePacket);

        clientSocket.close();
        System.out.println("RECEBIDO = " + Arrays.toString(receivePacket.getData()));

        try {
            byte[] key = aes.decrypt(makeHash(code), Base64.decode(read(ReadWriteInfo.KEY), Base64.NO_WRAP));

            byte[] recv = aes.decrypt(key, Arrays.copyOf(receivePacket.getData(), receivePacket.getLength()));

            System.out.println("RECEBIDO LIMPO = " + Arrays.toString(recv));

            //byte[] recv = aes.decrypt(read(ReadWriteInfo.KEY), receivePacket.getData());
            byte[] recv_hash = Arrays.copyOfRange(recv, 0, 16);
            byte[] message = Arrays.copyOfRange(recv, 16, recv.length);

            System.out.println("HASH = " + Arrays.toString(recv_hash));

            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hash = digest.digest(message);
            byte[] cappedHash = Arrays.copyOfRange(hash, 8, 24);
            System.out.println("CAPPED = " + Arrays.toString(cappedHash));

            if (Arrays.equals(cappedHash, recv_hash)) {
                return recv;
            } else {
                System.out.println("HASH NOT MATCH");
                return new byte[0];
            }

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | InvalidParameterSpecException | InvalidKeyException | InvalidKeySpecException | BadPaddingException e) {
            e.printStackTrace();
        }
        System.out.println("ERROR IN RECEIVE");
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
