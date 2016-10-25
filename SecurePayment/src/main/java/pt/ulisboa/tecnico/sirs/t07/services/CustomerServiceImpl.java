package pt.ulisboa.tecnico.sirs.t07.services;

import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.sirs.t07.models.Customer;
import pt.ulisboa.tecnico.sirs.t07.dao.CustomerDAO;

import java.util.List;

/**
 * Created by tiago on 25/10/2016.
 */
public class CustomerServiceImpl implements CustomerService{

    private CustomerDAO customerDAO;

    @Override
    @Transactional
    public void addCustomer(Customer customer) {
        this.customerDAO.addCustomer(customer);
    }

    @Override
    @Transactional
    public void updateCustomer(Customer customer) {
        this.customerDAO.updateCustomer(customer);
    }

    @Override
    @Transactional
    public Customer findByPhoneNumber(String phoneNumber) {
        return this.customerDAO.findByPhoneNumber(phoneNumber);
    }

    @Override
    @Transactional
    public List<Customer> list(){
        return this.customerDAO.list();
    }

    @Override
    @Transactional
    public void removeCustomerByPhoneNumber(String phoneNumber) {
        this.customerDAO.removeCustomerByPhoneNumber(phoneNumber);
    }
}
