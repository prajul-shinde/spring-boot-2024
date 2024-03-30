package com.omsai.spring6reactive.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omsai.spring6reactive.config.DatabaseConfig;
import com.omsai.spring6reactive.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.context.annotation.Import;

@DataR2dbcTest
@Import(DatabaseConfig.class)
public class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void testCreateJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(getTestCustomer()));
    }

    @Test
    void testSaveNewCustomer() {

        customerRepository.save(getTestCustomer()).subscribe(savedCustomer -> System.out.println(savedCustomer.toString()));
    }

    public static Customer getTestCustomer() {

        return Customer.builder()
                .customerName("prajul")
                .build();
    }
}