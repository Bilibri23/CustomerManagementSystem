package com.brian.cms.service;

import com.brian.cms.Exception.DuplicateResourceException;
import com.brian.cms.Exception.RequestValidationException;
import com.brian.cms.Exception.ResourceNotFoundException;
import com.brian.cms.model.Customer;
import com.brian.cms.model.CustomerRegistrationRequest;
import com.brian.cms.model.UpdateCustomerRegistrationRequest;
import com.brian.cms.repository.CustomerDao;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
    @Mock
    private  CustomerDao customerDao;
    private CustomerServiceImpl underTest;
    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
         underTest = new CustomerServiceImpl(customerDao);
    }


    @Test
    void getAllCustomers() {
        //when
        underTest.getAllCustomers();

        //then
        Mockito.verify(customerDao).findAll();
    }

    @Test
    void canGetCustomerByID() {
        //Given
        int id = 10;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );
        when(customerDao.findById(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomerByID(10);

        // Then
        assertThat(actual).isEqualTo(customer);
    }
    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        //Given
        int id = 10;
        when(customerDao.findById(id)).thenReturn(Optional.empty());

        //When
        // Then
        assertThatThrownBy(()->underTest.getCustomerByID(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(
                        "The customer  not found with  id [%s]".formatted(id)
                );
    }

//    @Test
//    void addCustomer() {
//        //Given
//        String email = "alex@gmail.com";
//        when(customerService.existsPersonWithEmail(email)).thenReturn(false);
//
//        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
//                "Alex", email, 19
//        );
//        //When
//        underTest.addCustomer(request);
//
//        //Then
//        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
//                Customer.class
//        );
//
//        Mockito.verify(customerService).saveCustomer(customerArgumentCaptor.capture());
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//        assertThat(capturedCustomer.getId()).isNull();
//        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
//        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
//        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
//
//    }@Test
//    void WillThrowWhenEmailExistsWhileAddingACustomer() {
//        //Given
//        String email = "alex@gmail.com";
//        when(customerService.existsPersonWithEmail(email)).thenReturn(true);
//
//        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
//                "Alex", email, 19
//        );
//        //When
//        assertThatThrownBy(()->underTest.addCustomer(request))
//                .isInstanceOf(DuplicateResourceException.class)
//                .hasMessage("email taken already");
//        //then
////below says never inserts a customer due to the exception that is thrown
//        Mockito.verify(customerService, never()).saveCustomer(any());
//
//
//    }
//
//    @Test
//    void deleteCustomer() {
//        //given
//        int id = 10;
//        //when
//        when(customerService.existsPersonWithID(id)).thenReturn(true);
//        underTest.deleteCustomer(id);
//        //then
//        Mockito.verify(customerDao).deleteById(id);
//    }
//
//    @Test
//    void willThrowDeleteCustomerByIdNotExist() {
//        //given
//        int id = 10;
//        //when
//        when(customerService.existsPersonWithID(id)).thenReturn(false);
//
//        assertThatThrownBy(()->underTest.deleteCustomer(id))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessage("This customer does not exist!");
//        //then
//        Mockito.verify(customerService, never()).deleteCustomer(id)
//        ;
//
//    }

//    @Test
//    void updateCustomer() {
//        //given
//        int id = 10;
//        Customer customer = new Customer(
//                id, "Alex", "alex@gmail.com", 19
//        );
//        when(customerDao.findById(id)).thenReturn(Optional.of(customer));
//        String newEmail = "Alex@Gmail.com";
//
//        UpdateCustomerRegistrationRequest updateRequest = new UpdateCustomerRegistrationRequest("Alex", newEmail, 19);
//
//        when(customerService.existsPersonWithEmail(newEmail)).thenReturn(false);
//
//        //when
//         underTest.UpdateCustomer(id, updateRequest);
//
//        //then
//        ArgumentCaptor<Customer> customerArgumentCaptor =
//                ArgumentCaptor.forClass(Customer.class);
//
//       Mockito.verify(customerService).UpdateCustomer(id,updateRequest);
//       Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//       assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
//       assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
//       assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
//    }
//    @Test
//    void canUpdateAllCustomersProperties() {
//        // Given
//        int id = 10;
//        Customer customer = new Customer(
//                id, "Alex", "alex@gmail.com", 19
//        );
//        when(customerDao.findById(id)).thenReturn(Optional.of(customer));
//
//        String newEmail = "alexandro@amigoscode.com";
//
//        UpdateCustomerRegistrationRequest updateRequest = new UpdateCustomerRegistrationRequest(
//                "Alexandro", newEmail, 23);
//
//        when(customerService.existsPersonWithEmail(newEmail)).thenReturn(false);
//
//        // When
//        underTest.UpdateCustomer(id, updateRequest);
//
//        // Then
//        ArgumentCaptor<Customer> customerArgumentCaptor =
//                ArgumentCaptor.forClass(Customer.class);
//
//        Mockito.verify(customerService).UpdateCustomer(id,updateRequest);
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
//        assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
//        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
//    }
//
//    @Test
//    void canUpdateOnlyCustomerName() {
//        // Given
//        int id = 10;
//        Customer customer = new Customer(
//                id, "Alex", "alex@gmail.com", 19
//        );
//        when(customerDao.findById(id)).thenReturn(Optional.of(customer));
//
//        UpdateCustomerRegistrationRequest updateRequest = new UpdateCustomerRegistrationRequest(
//                "Alexandro", null, null);
//
//        // When
//        underTest.UpdateCustomer(id, updateRequest);
//
//        // Then
//        ArgumentCaptor<Customer> customerArgumentCaptor =
//                ArgumentCaptor.forClass(Customer.class);
//
//        verify(customerService).UpdateCustomer(id,updateRequest);
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//        assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
//        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
//        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
//    }

//    @Test
//    void canUpdateOnlyCustomerEmail() {
//        // Given
//        int id = 10;
//        Customer customer = new Customer(
//                id, "Alex", "alex@gmail.com", 19
//        );
//        when(customerDao.findById(id)).thenReturn(Optional.of(customer));
//
//        String newEmail = "alexandro@amigoscode.com";
//
//        UpdateCustomerRegistrationRequest updateRequest = new UpdateCustomerRegistrationRequest(
//                null, newEmail, null);
//
//        when(customerService.existsPersonWithEmail(newEmail)).thenReturn(false);
//
//        // When
//        underTest.UpdateCustomer(id, updateRequest);
//
//        // Then
//        ArgumentCaptor<Customer> customerArgumentCaptor =
//                ArgumentCaptor.forClass(Customer.class);
//
//        verify(customerService).UpdateCustomer(id,updateRequest);
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
//        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
//        assertThat(capturedCustomer.getEmail()).isEqualTo(newEmail);
//    }
//
//    @Test
//    void canUpdateOnlyCustomerAge() {
//        // Given
//        int id = 10;
//        Customer customer = new Customer(
//                id, "Alex", "alex@gmail.com", 19
//        );
//        when(customerDao.findById(id)).thenReturn(Optional.of(customer));
//
//        UpdateCustomerRegistrationRequest updateRequest = new UpdateCustomerRegistrationRequest(
//                null, null, 22);
//
//        // When
//        underTest.UpdateCustomer(id, updateRequest);
//
//        // Then
//        ArgumentCaptor<Customer> customerArgumentCaptor =
//                ArgumentCaptor.forClass(Customer.class);
//
//        verify(customerService).UpdateCustomer(id,updateRequest);
//        Customer capturedCustomer = customerArgumentCaptor.getValue();
//
//        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
//        assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
//        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
//    }
//
//    @Test
//    void willThrowWhenTryingToUpdateCustomerEmailWhenAlreadyTaken() {
//        // Given
//        int id = 10;
//        Customer customer = new Customer(
//                id, "Alex", "alex@gmail.com", 19
//        );
//        when(customerDao.findById(id)).thenReturn(Optional.of(customer));
//
//        String newEmail = "alexandro@amigoscode.com";
//
//        UpdateCustomerRegistrationRequest updateRequest = new UpdateCustomerRegistrationRequest(
//                null, newEmail, null);
//
//        when(customerService.existsPersonWithEmail(newEmail)).thenReturn(true);
//
//        // When
//        assertThatThrownBy(() -> underTest.UpdateCustomer(id, updateRequest))
//                .isInstanceOf(DuplicateResourceException.class)
//                .hasMessage("email already taken");
//
//        // Then
//       Mockito.verify(customerService, never()).UpdateCustomer(id, any());
//    }
//
//    @Test
//    void willThrowWhenCustomerUpdateHasNoChanges() {
//        // Given
//        int id = 10;
//        Customer customer = new Customer(
//                id, "Alex", "alex@gmail.com", 19
//        );
//        when(customerDao.findById(id)).thenReturn(Optional.of(customer));
//
//        UpdateCustomerRegistrationRequest updateRequest = new UpdateCustomerRegistrationRequest(
//                customer.getName(), customer.getEmail(), customer.getAge());
//
//        // When
//        assertThatThrownBy(() -> underTest.UpdateCustomer(id, updateRequest))
//                .isInstanceOf(RequestValidationException.class)
//                .hasMessage("no data changes found");
//
//        // Then
//        verify(customerService, never()).UpdateCustomer(id,any());
//    }
}