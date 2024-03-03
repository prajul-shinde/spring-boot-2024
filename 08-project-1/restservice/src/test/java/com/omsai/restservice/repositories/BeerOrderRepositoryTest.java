package com.omsai.restservice.repositories;

import com.omsai.restservice.entities.Beer;
import com.omsai.restservice.entities.BeerOrder;
import com.omsai.restservice.entities.BeerOrderShipment;
import com.omsai.restservice.entities.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer testCustomer;

    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().getFirst();
        testBeer = beerRepository.findAll().getFirst();
    }

    @Transactional
    @Test
    void testBeerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("test order")
                .beerOrderShipment(BeerOrderShipment.builder()
                        .trackingNumber("1234r")
                        .build())
                .customer(testCustomer)
                .build();
        BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);
        System.out.println(savedBeerOrder);
    }

}