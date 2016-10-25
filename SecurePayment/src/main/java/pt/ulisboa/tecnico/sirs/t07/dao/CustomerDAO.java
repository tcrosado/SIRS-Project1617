package pt.ulisboa.tecnico.sirs.t07.dao;


import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.sirs.t07.models.Customer;

import java.util.List;

/**
 * Created by tiago on 24/10/2016.
 */
@Transactional
public interface CustomerDAO{
    void addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    Customer findByPhoneNumber(String phoneNumber);
    List<Customer> list();
    void removeCustomerByPhoneNumber(String phoneNumber);
}
