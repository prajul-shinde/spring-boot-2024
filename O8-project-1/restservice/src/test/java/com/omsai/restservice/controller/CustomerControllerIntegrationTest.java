package com.omsai.restservice.controller;

import com.omsai.restservice.entities.Customer;
import com.omsai.restservice.mappers.CustomerMapper;
import com.omsai.restservice.model.CustomerDTO;
import com.omsai.restservice.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIntegrationTest {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testListCustomers() {

        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testListCustomersEmptyCustomers() {

        customerRepository.deleteAll();

        List<CustomerDTO> dtos = customerController.listCustomers();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetCustomerById() {

        Customer customer = customerRepository.findAll().getFirst();

        CustomerDTO dto = customerController.getCustomerById(customer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testCustomerByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewCustomer() {

        CustomerDTO customer = CustomerDTO.builder().customerName("New Customer").build();
        ResponseEntity responseEntity = customerController.handlePost(customer);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(location[4]);
        Customer savedCustomer = customerRepository.findById(savedUUID).get();
        assertThat(savedCustomer).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomerById() {

        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        customerDTO.setCustomerName("Updated");
        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo("Updated");

    }

    @Test
    void testUpdateCustomerByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerById() {

        Customer customer = customerRepository.findAll().getFirst();
        ResponseEntity responseEntity = customerController.deleteCustomerById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId()).isEmpty());

    }

    @Test
    void testDeleteCustomerByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            customerController.deleteCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testPatchCustomerById() {

        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);
        customerDTO.setId(null);
        customerDTO.setVersion(null);
        customerDTO.setCustomerName("Patched");
        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Customer updatedCustomer = customerRepository.findById(customer.getId()).get();
        assertThat(updatedCustomer.getCustomerName()).isEqualTo("Patched");
    }

    @Test
    void testPatchCustomerByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            customerController.patchCustomerById(UUID.randomUUID(), CustomerDTO.builder().build());
        });
    }
}