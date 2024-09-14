package com.brian.cms;

import com.brian.cms.model.Customer;
import com.brian.cms.repository.CustomerDao;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
// we are using our testing container and not the actual DB

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerDaoTest extends AbstractTestContainers {
    @Autowired
    private CustomerDao underTest;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

//    @Test
//    void existsCustomerByEmail() {
//        //GIVEN
//        String email = Faker.instance().internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                Faker.instance().name().fullName(),
//                email,
//                20
//        );
//        //when
//        underTest.save(customer);
//
//
//        var actual = underTest.existsCustomerByEmail(email);
//
//        //then
//        assertThat(actual).isTrue();
//
//    }

//    @Test
//    void existsCustomerByEmailFailsWhenEmailAbsent() {
//        //GIVEN
//        String email = Faker.instance().internet().safeEmailAddress() + "-" + UUID.randomUUID();
//
//        //when
//
//        var actual  = underTest.existsCustomerByEmail(email);
//
//        //then
//        assertThat(actual).isFalse();
//    }
//
//    @Test
//    void existsCustomerById() {
//        //GIVEN
//        String email = Faker.instance().internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                Faker.instance().name().fullName(),
//                email,
//                20
//        );
//        //when
//        underTest.save(customer);
//        //extracting the id
//        int id = underTest.findAll()
//                .stream()
//                .filter(c->c.getEmail().equals(email))
//                .map(Customer::getId)
//                .findFirst()
//                .orElseThrow();
//
//        var actual = underTest.existsCustomerById(id);
//
//        //then
//        assertThat(actual).isTrue();
//
//    }    @Test
//    void existsCustomerByIdWhenIdAbsent() {
//        //GIVEN
//        int id = -1;
//
//        //when
//        var actual = underTest.existsCustomerById(id);
//
//        //then
//        assertThat(actual).isFalse();
//
//    }
}