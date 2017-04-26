package pt.ulisboa.tecnico.sirs.t07.presentation;

import pt.ulisboa.tecnico.sirs.t07.service.UDPConnectionSetUp;

import java.net.SocketException;
import java.util.Optional;

/**
 * Created by tiago on 12/11/2016.
 */
public class UDPConnectionManager implements AbstractView{
    /**
     * Toda a logica de escolher o serviço e estestablecer ligações
     */

    private UDPConnectionSetUp service;

    public UDPConnectionManager() throws SocketException {
        this.service= new UDPConnectionSetUp(2000, Optional.empty());
    }



    public void execute(){
        this.service.execute();
    }
}
