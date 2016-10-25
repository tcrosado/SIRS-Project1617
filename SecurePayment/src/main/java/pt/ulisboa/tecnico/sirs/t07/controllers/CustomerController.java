package pt.ulisboa.tecnico.sirs.t07.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.ulisboa.tecnico.sirs.t07.models.Customer;
import pt.ulisboa.tecnico.sirs.t07.models.dao.CustomerDao;


/**
 * Created by tiago on 24/10/2016.
 */
@Controller
public class CustomerController {
    @RequestMapping("/create")
    @ResponseBody
    public String create(String name,String phoneNumber) {
        String customerId= "";

        try{
            Customer customer = new Customer(name,phoneNumber);
            customerDao.save(customer);
            customerId = String.valueOf(customer.getId());
        }
        catch (Exception ex) {
            return "Error creating the user: " + ex.toString();
        }
        return "User succesfully created with id = " + customerId;
    }

    @RequestMapping("/getByPhoneNumber")
    @ResponseBody
    public String getByPhoneNumber(String phoneNumber) {
        String customerId = "";

        try{
            Customer customer = customerDao.findByPhoneNumber(phoneNumber);
            customerId = String.valueOf(customer.getId());
        }
        catch (Exception ex) {
            return "Customer not found";
        }
        return "The customer id is: " + customerId;
    }

    @Autowired
    private CustomerDao customerDao;

}
