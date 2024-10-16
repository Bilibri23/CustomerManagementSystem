package com.brian.cms.Controller;


import Dto.CustomerDTO;
import com.brian.cms.Jwt.JWTUtil;
import com.brian.cms.model.Customer;
import com.brian.cms.model.CustomerRegistrationRequest;
import com.brian.cms.service.CustomerService;
import com.brian.cms.model.UpdateCustomerRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;


    @GetMapping()
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(){
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }
    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable(value = "id") Integer ID){
       CustomerDTO customer = customerService.getCustomerByID(ID);
       return ResponseEntity.ok(customer);

    }

    @PostMapping()
    public ResponseEntity<?> registerCustomer( @RequestBody CustomerRegistrationRequest customer){
        customerService.addCustomer(customer);
        String jwtToken = jwtUtil.issueToken(customer.email(), "ROLE_USER");
        return ResponseEntity.ok()
                .header(HttpHeaders.AUTHORIZATION,jwtToken)
                .build();

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> DeleteCustomer(@PathVariable Integer id){
        customerService.deleteCustomer(id);
        return ResponseEntity.ok("Delete successfully");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable Integer id, @RequestBody UpdateCustomerRegistrationRequest updateCustomerRegistrationRequest){
        customerService.UpdateCustomer(id,updateCustomerRegistrationRequest);
        return ResponseEntity.ok("Updated successfully");
    }
}
