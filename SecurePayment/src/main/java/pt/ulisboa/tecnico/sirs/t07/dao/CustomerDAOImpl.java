package pt.ulisboa.tecnico.sirs.t07.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pt.ulisboa.tecnico.sirs.t07.models.Customer;

import java.util.List;

/**
 * Created by tiago on 25/10/2016.
 */
@Repository
public class CustomerDAOImpl implements CustomerDAO {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDAOImpl.class);

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sf){
        this.sessionFactory = sf;
    }

    @Override
    public void addCustomer(Customer customer){
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(customer);
        logger.info("Customer saved successfully, Customer Details="+customer);
    }

    @Override
    public void updateCustomer(Customer customer){
        Session session = this.sessionFactory.getCurrentSession();
        session.update(customer);
        logger.info("Customer updated successfully, Customer Details="+customer);
    }

    @Override
    public Customer findByPhoneNumber(String phoneNumber){
        Session session = this.sessionFactory.getCurrentSession();
        Customer c = (Customer) session.load(Customer.class, new String(phoneNumber));
        logger.info("Customer loaded successfully, Customer details="+c);
        return c;
    }

    @Override
    public List<Customer> list(){
        Session session = this.sessionFactory.getCurrentSession();
        List<Customer> customerList =  session.createQuery("FROM custumer").list();
        for(Customer c: customerList){
             logger.info("Customer List::"+c);
        }

        return customerList;
    }
    @Override
    public void removeCustomerByPhoneNumber(String phoneNumber){
        Session session = this.sessionFactory.getCurrentSession();
        Customer c = (Customer) session.load(Customer.class, new String(phoneNumber));
        if(null != c){
            session.delete(c);
        }
        logger.info("Customer deleted successfully, customer details="+c);
    }
}
