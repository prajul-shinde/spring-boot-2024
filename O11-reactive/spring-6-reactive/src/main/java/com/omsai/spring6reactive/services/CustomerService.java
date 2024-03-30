package com.omsai.spring6reactive.services;

import com.omsai.spring6reactive.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<CustomerDTO> listCustomers();

    Mono<CustomerDTO> getCustomerById(final Integer customerId);

    Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO);

    Mono<CustomerDTO> updateCustomer(final Integer customerId, CustomerDTO customer);

    Mono<CustomerDTO> patchCustomer(final Integer customerId, com.omsai.spring6reactive.model.CustomerDTO customerDTO);

    Mono<Void> deleteCustomerById(final Integer customerId);
}
