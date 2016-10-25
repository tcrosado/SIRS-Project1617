package pt.ulisboa.tecnico.sirs.t07.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import pt.ulisboa.tecnico.sirs.t07.models.Customer;
import pt.ulisboa.tecnico.sirs.t07.dao.CustomerDAO;
import pt.ulisboa.tecnico.sirs.t07.services.CustomerService;


/**
 * Created by tiago on 24/10/2016.
 */
@Controller
public class CustomerController {


    private CustomerService customerService;

    @Autowired(required = true)
    @Qualifier(value="customerService")
    public void setCustomerService(CustomerService cs){
        this.customerService = cs;
    }

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public String list(Model model){
        model.addAttribute("customer",new Customer());
        model.addAttribute("listCustomers",this.customerService.list());
        return "customer";
    }

}
