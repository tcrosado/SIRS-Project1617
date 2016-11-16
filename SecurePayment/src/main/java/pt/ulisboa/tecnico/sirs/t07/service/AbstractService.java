package pt.ulisboa.tecnico.sirs.t07.service;

/**
 * Created by tiago on 12/11/2016.
 */
abstract public class AbstractService {

    abstract void dispatch();

    public void execute(){
        this.dispatch();
    }
}
