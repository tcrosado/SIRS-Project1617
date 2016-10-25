package pt.ulisboa.tecnico.sirs.t07.services;

import pt.ulisboa.tecnico.sirs.t07.models.Customer;

import java.util.List;

/**
 * Created by tiago on 25/10/2016.
 */
public interface CustomerService {
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    Customer findByPhoneNumber(String phoneNumber);
    List<Customer> list();
    void removeCustomerByPhoneNumber(String phoneNumber);
}
