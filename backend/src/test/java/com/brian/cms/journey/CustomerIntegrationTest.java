package com.brian.cms.journey;

import Dto.CustomerDTO;
import com.brian.cms.model.Customer;
import com.brian.cms.model.CustomerRegistrationRequest;
import com.brian.cms.model.Gender;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;  //used in computations where u want to interact with a database or external service


import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


//WebTest package permits us to simulate our test environment as tho it is that of production
//we will need flux to use it, web test client will be our postman

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) //since by default it does not start a web server
public class CustomerIntegrationTest {

    public static final String CUSTOMER_URI = "/api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;
    // In integration test we don't invoke the methods directly, but we send http request

    private static final Random random = new Random();

    @Test
    void canRegisterACustomer(){
        //create registration request
        Faker faker = new Faker();
        String Name = faker.name().fullName();
        String email = faker.name().fullName() + "-" + UUID.randomUUID() + "@noblesse.com";
        int age =  random.nextInt(1,100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
            Name, email, age,Gender.MALE,"password"
        );

        //SendAPostRequest
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange() //like press send on postman, send request
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);


        //get all Customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();

        int id = allCustomers.stream().filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id).findFirst().orElseThrow();


        CustomerDTO expectedCustomer = new CustomerDTO(
                null,
                Name, email,
                Gender.MALE, age,List.of("ROLE_USER"), email);

        //make sure that customer is present
        assertThat(allCustomers)
                .contains(expectedCustomer);

        //get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .isEqualTo(expectedCustomer);


    }
    @Test
    void canDeleteACustomer(){
        //create registration request
        Faker faker = new Faker();
        String Name = faker.name().fullName();
        String email = faker.name().fullName() + "-" + UUID.randomUUID() + "@noblesse.com";

        int age =  random.nextInt(1,100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                Name, email,age,Gender.MALE,"password"
        );

        CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(
                Name, email+".uk",age,Gender.MALE,"password"
        );

        //SendAPostRequest
        // send a post request to create customer 1
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange() //like press send on postman, send request
                .expectStatus()
                .isOk();


        // send a post request to create customer 2
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegistrationRequest.class)
                .exchange() //like press send on postman, send request
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);


        //get all Customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {
                })
                .returnResult()
                .getResponseBody();



        int id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();


        // customer 2 deletes customer 1
    webTestClient.delete()
                    .uri(CUSTOMER_URI + "/{id}", id)
                    .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
                    .accept(MediaType.APPLICATION_JSON)
                            .exchange()
                                    .expectStatus()
                                            .isOk();

        //customer 2 gets customer1 by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();

    }
//@Test
//    void canUpdateACustomer(){
//        //create registration request
//        Faker faker = new Faker();
//        String name = faker.name().fullName();
//        String email = faker.name().fullName() + "-" + UUID.randomUUID() + "@noblesse.com";
//
//        int age =  random.nextInt(1,100);
//
//        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
//                name, email,age
//        );
//
//        //SendAPostRequest
//        webTestClient.post()
//                .uri(CUSTOMER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(Mono.just(request), CustomerRegistrationRequest.class)
//                .exchange() //like press send on postman, send request
//                .expectStatus()
//                .isCreated();
//
//
//
//        //get all Customers
//        List<Customer> allCustomers = webTestClient.get()
//                .uri(CUSTOMER_URI)
//                .accept(MediaType.APPLICATION_JSON)
//                .exchange()
//                .expectStatus()
//                .isOk()
//                .expectBodyList(new ParameterizedTypeReference<Customer>() {
//                })
//                .returnResult()
//                .getResponseBody();
//
//
//
//        int id = allCustomers.stream().filter(customer -> customer.getEmail().equals(email))
//                .map(Customer::getId).findFirst().orElseThrow();
//
//
//    // Create an update request with different data
//    String newName = "Ali Updated"; // Changed to ensure it is different
//    String newEmail = email; // Keep email the same
//    int newAge = age + 1; // Change age to ensure it is different
//
//    UpdateCustomerRegistrationRequest updateRequest = new UpdateCustomerRegistrationRequest(
//            newName, newEmail, newAge
//    );
//
//        //update Request
//    webTestClient.put()
//                    .uri(CUSTOMER_URI + "/{id}", id)
//                    .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).body(Mono.just(updateRequest),UpdateCustomerRegistrationRequest.class)
//
//                            .exchange()
//                                    .expectStatus()
//                                            .isOk();
//
//        //get customer by id
//    Customer updatedCustomer = webTestClient.get()
//            .uri(CUSTOMER_URI + "/{id}", id)
//            .accept(MediaType.APPLICATION_JSON)
//            .exchange()
//            .expectStatus()
//            .isOk()
//            .expectBody(Customer.class)
//            .returnResult()
//            .getResponseBody();
//
//    Customer expected = new Customer(
//            id, newName, newEmail, newAge
//    );
//
//    assertThat(updatedCustomer).isNotEqualTo(expected);
//
//}

}
