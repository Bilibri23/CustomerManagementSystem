package com.brian.cms.service;

import com.brian.cms.Exception.DuplicateResourceException;
import com.brian.cms.Exception.RequestValidationException;
import com.brian.cms.Exception.ResourceNotFoundException;
import com.brian.cms.model.Customer;
import com.brian.cms.model.CustomerRegistrationRequest;
import com.brian.cms.model.UpdateCustomerRegistrationRequest;
import com.brian.cms.repository.CustomerDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl  implements CustomerService {

    private final CustomerDao customerDao;
    /** private final CustomerService customerService;  lol remove this, the class implements already customer service, how can u
      make it to depend on it again, it creates circular reference, as customer controller depends on service and impl depends on service again
     meanwhile it is the implementation,
     */


    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    @Override
    public Customer getCustomerByID(Integer id) {
        return customerDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("The customer  not found with  id [%s]".formatted(id)));
    }

    @Override
    public void saveCustomer(Customer customer) {
        customerDao.save(customer);
    }

    public void addCustomer(
            CustomerRegistrationRequest customerRegistrationRequest
    ) {
        //check if email exists
        String email = customerRegistrationRequest.email();
        if (existsPersonWithEmail(email)) {
            throw new DuplicateResourceException("email taken already");
        }
        //add
        saveCustomer(
                new Customer(customerRegistrationRequest.name(),
                        customerRegistrationRequest.email(),
                        customerRegistrationRequest.age()
                ));
    }


    @Override
    public boolean existsPersonWithEmail(String email) {
        return customerDao.existsCustomerByEmail(email);
    }

    @Override
    public boolean existsPersonWithID(Integer id) {
        return customerDao.existsCustomerById(id);
    }

    @Override
    public void deleteCustomer(Integer id) {
        //check if the customer exist
        if (!existsPersonWithID(id)) {
            throw new ResourceNotFoundException("This customer does not exist!");
        }
        //delete
        customerDao.deleteById(id);

    }

    @Override
    public void UpdateCustomer(Integer id, UpdateCustomerRegistrationRequest updateRequest) {

        Customer customer = getCustomerByID(id);
        boolean changes = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changes = true;
        }
        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changes = true;
        }
        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if(existsPersonWithEmail(updateRequest.email())){
                throw  new DuplicateResourceException("email already taken");
            }
            customer.setEmail(updateRequest.email());
            changes = true;
        }
        if(!changes){
            throw  new RequestValidationException("NO Data changes found, u made no changes bro!");
        }


        customerDao.save(customer);

    }
}
