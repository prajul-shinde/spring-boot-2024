package com.omsai.spring6reactive.controllers;

import com.omsai.spring6reactive.model.CustomerDTO;
import com.omsai.spring6reactive.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    public static final String CUSTOMER_PATH = "/api/v2/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    @GetMapping(CUSTOMER_PATH)
    Flux<CustomerDTO> getAllCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    Mono<CustomerDTO> getCustomerById(@PathVariable("customerId") final Integer customerId) {

        return customerService.getCustomerById(customerId).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
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

        return customerService.updateCustomer(customerId, customer)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(updatedCustomer -> ResponseEntity.noContent().build());
    }

    @PatchMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<CustomerDTO>> patchCustomer(@PathVariable("customerId") final Integer customerId, @Validated @RequestBody CustomerDTO customerDTO) {

        return customerService.patchCustomer(customerId, customerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(patchedCustomer -> ResponseEntity.ok().build());
    }

    @DeleteMapping(CUSTOMER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteCustomerById(@PathVariable("customerId") final Integer customerId) {

        return customerService.getCustomerById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(customerDTO -> customerService.deleteCustomerById(customerDTO.getId()))
                .thenReturn(ResponseEntity.noContent().build());
    }
}

