package pt.ulisboa.tecnico.sirs.t07.models.dao;


import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.ulisboa.tecnico.sirs.t07.models.Customer;

/**
 * Created by tiago on 24/10/2016.
 */
@Transactional
public interface CustomerDao extends CrudRepository<Customer, Long> {
    Customer findByPhoneNumber(String phoneNumber);
}
