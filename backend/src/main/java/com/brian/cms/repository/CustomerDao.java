package com.brian.cms.repository;

import com.brian.cms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerDao  extends JpaRepository<Customer, Integer> {

 boolean existsCustomerByEmail(String email);
 boolean existsCustomerById(Integer id);
 Optional<Customer> findCustomerByEmail(String email);
}
