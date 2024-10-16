package com.brian.cms.service;

import Dto.CustomerDTO;
import com.brian.cms.Exception.DuplicateResourceException;
import com.brian.cms.Exception.RequestValidationException;
import com.brian.cms.Exception.ResourceNotFoundException;
import com.brian.cms.model.Customer;
import com.brian.cms.model.CustomerRegistrationRequest;
import com.brian.cms.model.Gender;
import com.brian.cms.model.UpdateCustomerRegistrationRequest;
import com.brian.cms.repository.CustomerDao;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl  implements CustomerService {

    private final CustomerDao customerDao;
    private final CustomerDTOMapper customerDTOMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerServiceImpl(CustomerDao customerDao,CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder){
        this.customerDao = customerDao;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder =passwordEncoder;
    }


    /** private final  customerService;  lol remove this, the class implements already customer service, how can u
      make it to depend on it again, it creates circular reference, as customer controller depends on service and impl depends on service again
     meanwhile it is the implementation,
     */



    @Override
    public List<CustomerDTO> getAllCustomers() {
        //mapping each row of type customer to CustomerDTO for the client
        return customerDao.findAll().stream().map(customerDTOMapper)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomerByID(Integer id) {
        return customerDao.findById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("The customer  not found with  id [%s]".formatted(id)));
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
                        customerRegistrationRequest.age(),
                        Gender.MALE, passwordEncoder.encode(customerRegistrationRequest.password())));
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
    public void UpdateCustomer(Integer customerId, UpdateCustomerRegistrationRequest updateRequest) {

       Customer customer = customerDao.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("The customer  not found with  id [%s]".formatted(customerId)));

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

    @Override
    public Optional<Customer> selectedUserByEmail(String email) {
        return customerDao.findCustomerByEmail(email);
    }
}
