package com.brian.cms.service;

import com.brian.cms.model.Customer;

import com.brian.cms.model.CustomerRegistrationRequest;
import com.brian.cms.model.UpdateCustomerRegistrationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer getCustomerByID(Integer id);
    void saveCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    void addCustomer(CustomerRegistrationRequest customerRegistrationRequest);
    boolean existsPersonWithID(Integer id);
    void deleteCustomer(Integer id);
    void UpdateCustomer(Integer id, UpdateCustomerRegistrationRequest updateCustomerRegistrationRequest);

}

