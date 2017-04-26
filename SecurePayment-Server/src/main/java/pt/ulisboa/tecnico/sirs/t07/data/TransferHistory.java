package pt.ulisboa.tecnico.sirs.t07.data;

import java.sql.Timestamp;
import java.util.UUID;

/**
 * Created by trosado on 17/11/16.
 */
public class TransferHistory {
    private UUID tid;
    private Timestamp time;
    private String originIban;
    private String destinationIban;
    private int value;

    public TransferHistory(UUID tid,Timestamp time, String originIban, String destinationIban,int value){
        this.tid=tid;
        this.time=time;
        this.originIban=originIban;
        this.destinationIban=destinationIban;
        this.value=value;
    }

    public UUID getTid() {
        return tid;
    }


    public Timestamp getTime() {
        return time;
    }


    public String getOriginIban() {
        return originIban;
    }



    public String getDestinationIban() {
        return destinationIban;
    }



    public int getValue() {
        return value;
    }


}
