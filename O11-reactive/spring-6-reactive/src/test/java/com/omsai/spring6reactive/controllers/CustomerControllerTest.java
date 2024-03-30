package com.omsai.spring6reactive.controllers;

import com.omsai.spring6reactive.domain.Customer;
import com.omsai.spring6reactive.model.CustomerDTO;
import com.omsai.spring6reactive.repositories.CustomerRepositoryTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void testListCustomers() {

        webTestClient.get().uri(CustomerController.CUSTOMER_PATH)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .valueEquals("Content-type", "application/json")
                .expectBody()
                .jsonPath("$.size()").isEqualTo(3);
    }

    @Test
    @Order(2)
    void testGetCustomerById() {

        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus()
                .isOk()
                .expectHeader()
                .valueEquals("Content-type", "application/json")
                .expectBody(CustomerDTO.class);
    }

    @Test
    @Order(3)
    void testGetCustomerByIdNotFound() {

        webTestClient.get().uri(CustomerController.CUSTOMER_PATH_ID, 99)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(4)
    void testSaveNewCustomer() {

        webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectHeader()
                .location("http://localhost:8080/api/v2/customer/4");
    }

    @Test
    @Order(5)
    void testSaveNewCustomerBadRequest() {
        Customer testCustomer = CustomerRepositoryTest.getTestCustomer();
        testCustomer.setCustomerName("");
        webTestClient.post().uri(CustomerController.CUSTOMER_PATH)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @Order(6)
    void testUpdateCustomer() {

        webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(7)
    void testUpdateCustomerNotFound() {

        webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(8)
    void testUpdateCustomerBadRequest() {
        Customer testCustomer = CustomerRepositoryTest.getTestCustomer();
        testCustomer.setCustomerName("");
        webTestClient.put().uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .body(Mono.just(testCustomer), CustomerDTO.class)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    @Test
    @Order(9)
    void testDeleteCustomerById() {

        webTestClient.delete()
                .uri(CustomerController.CUSTOMER_PATH_ID, 1)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    @Order(10)
    void testPatchNotFound() {

        webTestClient.patch().uri(CustomerController.CUSTOMER_PATH_ID, 999)
                .body(Mono.just(CustomerRepositoryTest.getTestCustomer()), CustomerDTO.class)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    @Order(11)
    void testDeleteCustomerByIdNotFound() {

        webTestClient.delete()
                .uri(CustomerController.CUSTOMER_PATH_ID, 123)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}