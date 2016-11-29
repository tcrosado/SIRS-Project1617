package pt.ulisboa.tecnico.sirs.t07.utils;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.*;
import java.util.Optional;

/**
 * Created by trosado on 28/11/16.
 */
public class UDPConnection {

    private DatagramSocket socket;

    public UDPConnection(Integer port) throws SocketException {
        this.socket = new DatagramSocket(port);
    }

    public UDPConnection() throws SocketException {
        this.socket = new DatagramSocket();
    }

    public void sendData(byte[] message,InetAddress address,Integer port) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(message, message.length, address, port);
        socket.send(sendPacket);
    }

    public void sendData(byte[] message,String hostname,Integer port) throws IOException {
        InetAddress address = InetAddress.getByName(hostname);
        this.sendData(message,address,port);
    }

    public DatagramPacket receiveData() throws IOException {
        byte[] receiveData = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        socket.receive(receivePacket);

        return receivePacket;
    }

    public void closeConnection() {
        socket.close();
    }

    public int getPort(){ return this.socket.getLocalPort(); }

    public void setTimeout(Integer timeout) throws SocketException {
        this.socket.setSoTimeout(timeout);
    }

    public Integer getTimeout() throws SocketException {
        return this.socket.getSoTimeout();
    }

}

