package pt.ulisboa.tecnico.sirs.t07.models;



import com.sun.istack.internal.NotNull;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.*;

/**
 * Created by tiago on 24/10/2016.
 */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    public Customer() {   }

    public Customer(long id) {
        this.id =id;
    }

    public Customer(String name,String phoneNumber){
        this.name=name;
        this.phoneNumber=phoneNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
