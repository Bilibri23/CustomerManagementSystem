package com.brian.cms.repository;

import com.brian.cms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerDao  extends JpaRepository<Customer, Integer> {

 boolean existsCustomerByEmail(String email);
 boolean existsCustomerById(Integer id);
}
