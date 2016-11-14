package pt.ulisboa.tecnico.sirs.t07.service;

import java.net.DatagramPacket;
import java.util.AbstractQueue;
import java.util.PriorityQueue;
s

/**
 * Created by tiago on 12/11/2016.
 */
public class UDPStablishService extends AbstractService implements Runnable{

    //private AbstractQueue<DatagramPacket> packets;
    private Integer timeout;
    private DatagramPacket packet;

    public UDPStablishService(DatagramPacket packet,Integer timeout){
        //this.packets = new PriorityQueue<DatagramPacket>();
        this.packet = packet;
        this.timeout = timeout;
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

        this.packet.getData()

    }
}
