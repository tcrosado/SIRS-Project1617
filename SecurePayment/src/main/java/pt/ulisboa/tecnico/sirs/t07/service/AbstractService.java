package pt.ulisboa.tecnico.sirs.t07.service;

import pt.ulisboa.tecnico.sirs.t07.exceptions.ErrorMessageException;

/**
 * Created by tiago on 12/11/2016.
 */
abstract public class AbstractService {

    abstract void dispatch();

    public void execute(){
        this.dispatch();
    }
}
