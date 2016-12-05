package pt.ulisboa.tecnico.sirs.t07.service;

import java.util.UUID;
import java.util.Vector;

/**
 * Created by trosado on 05/12/16.
 */
public class ConfirmTransactionService extends AbstractService {

    private UUID tid;
    private Vector<Integer> matrixValues;

    ConfirmTransactionService(UUID tid, Vector<Integer> values){
        this.tid = tid;
        this.matrixValues = values;
    }

    @Override
    void dispatch() {

    }
}
