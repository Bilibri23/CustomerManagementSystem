package com.brian.cms.service;

import Dto.CustomerDTO;
import com.brian.cms.model.Customer;

import com.brian.cms.model.CustomerRegistrationRequest;
import com.brian.cms.model.UpdateCustomerRegistrationRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {

    List<CustomerDTO> getAllCustomers();
    CustomerDTO getCustomerByID(Integer id);
    void saveCustomer(Customer customer);
    boolean existsPersonWithEmail(String email);
    void addCustomer(CustomerRegistrationRequest customerRegistrationRequest);
    boolean existsPersonWithID(Integer id);
    void deleteCustomer(Integer id);
    void UpdateCustomer(Integer id, UpdateCustomerRegistrationRequest updateCustomerRegistrationRequest);
    Optional<Customer> selectedUserByEmail(String email);
}

