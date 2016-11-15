package pt.ulisboa.tecnico.sirs.t07;

import pt.ulisboa.tecnico.sirs.t07.presentation.UDPConnectionManager;

import java.io.IOException;


/**
 * Created by trosado on 03/11/16.
 */
public class ServerApplication {

    public static void main(String[] args) throws IOException {
       /* DatagramSocket serverSocket = new DatagramSocket(9876);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true)
        { */
            /**
            * TODO
            *  - Threads (cada cliente)
            *  - JDBC interface
            *  - enviar resposta até receber confirmação
            *  - tamanho maximo UDP
            * */


            /**
             * TODO
             *  Operações
             *  - Transferência
             *  - Histórico
             *  - Saldo
             *
             *  Respostas
             *  - Saldo Insuficiente
             *  - Operação repetida
             *  - Confirmação
             *  - Mensagem de Bloqueio
             */
    /*
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            String sentence = new String( receivePacket.getData());
            System.out.println("RECEIVED: " + sentence);




            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();
            String capitalizedSentence = sentence.toUpperCase();
            sendData = capitalizedSentence.getBytes();
            DatagramPacket sendPacket =
                    new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
        }
    */

        /**
         *
         * 1 ~ Espera de conexões (porta)
         * 2 - Se tiver conexão muda para serviço
         */

        UDPConnectionManager cm = new UDPConnectionManager();
        cm.execute();











/*
        CustomerData cd = new CustomerData();
        System.out.print(cd.ibanExists("NL73INGB0698363980"));
        cd.close();
*/
    }
}
