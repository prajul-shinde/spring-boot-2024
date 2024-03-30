package com.omsai.spring6reactive.controllers;

import com.omsai.spring6reactive.model.CustomerDTO;
import com.omsai.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private static final String CUSTOMER_PATH = "/api/v2/customer";
    private static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> getAllCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") final Integer customerId) {

        return customerService.getCustomerById(customerId);
    }

    @PostMapping(CUSTOMER_PATH)
    Mono<ResponseEntity<CustomerDTO>> createCustomer(@Validated @RequestBody CustomerDTO customerDTO) {
        return
                customerService.saveNewCustomer(customerDTO)
                        .map(savedDTO -> ResponseEntity.created(UriComponentsBuilder
                                        .fromHttpUrl("http://localhost:8080" + CUSTOMER_PATH + "/" + savedDTO.getId())
                                        .build()
                                        .toUri())
                                .build());
    }

    @PutMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<CustomerDTO>> updateExistingCustomer(@PathVariable("customerId") final Integer customerId, @Validated @RequestBody CustomerDTO customer) {

        return customerService.updateCustomer(customerId, customer).map(updatedCustomer -> ResponseEntity.ok().build());
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<CustomerDTO>> patchCustomer(@PathVariable("customerId") final Integer customerId, @Validated @RequestBody CustomerDTO customerDTO) {

        return customerService.patchCustomer(customerId, customerDTO).map(patchedCustomer -> ResponseEntity.ok().build());
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable("customerId") final Integer customerId) {

        return customerService.deleteCustomerById(customerId).map(response -> ResponseEntity.noContent().build());
    }
}

