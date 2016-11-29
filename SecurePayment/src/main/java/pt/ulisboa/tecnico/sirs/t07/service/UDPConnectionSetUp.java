package pt.ulisboa.tecnico.sirs.t07.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;
import pt.ulisboa.tecnico.sirs.t07.utils.UDPConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.Vector;

/**
 * Created by tiago on 12/11/2016.
 */
public class UDPConnectionSetUp extends AbstractService {

    private final Logger logger = LoggerFactory.getLogger(UDPConnectionSetUp.class);

    private UDPConnection conn;
    //private DatagramSocket socket;
    private Integer timeout;
    private Integer threadLimit;
    private Integer distributionPosition;
    private Vector<Thread> threadList;

    public UDPConnectionSetUp(Integer timeout, Optional<Integer> port) throws SocketException {
        this.conn = new UDPConnection(5000);
        this.timeout = timeout;
        this.threadLimit = 1;
        this.distributionPosition = 0;
        this.threadList = new Vector<Thread>();
    }


    @Override
    void dispatch() {

        DatagramPacket packet;

        logger.info("Listening on UDP port {} for commands.",conn.getPort());

        while(true){
            //FIXME
            try {
                packet = conn.receiveData(); // Esta a espera de pedidos

                UDPEstablishService establish =  new UDPEstablishService(this.conn,packet,timeout);
                establish.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Assim que receber um pedido tem de criar uma thread ou mete em fila numa já existente para processar o pedido
            //if(threadList.size()<threadLimit){
                //Thread thread = new Thread(new UDPEstablishService(this.socket,packet,timeout));
                //threadList.add(thread);
            /*}else{
                Thread t = threadList.get(distributionPosition);//FIXME
            }
            distributionPosition = (distributionPosition+1)%threadLimit;
            */


        }





    }
}
