package com.omsai.spring6reactive.services;

import com.omsai.spring6reactive.mappers.CustomerMapper;
import com.omsai.spring6reactive.model.CustomerDTO;
import com.omsai.spring6reactive.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Flux<CustomerDTO> listCustomers() {
        return customerRepository.findAll().map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(final Integer customerId) {
        return customerRepository.findById(customerId).map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> saveNewCustomer(CustomerDTO customerDTO) {
        return customerRepository.save(customerMapper.customerDTOToCustomer(customerDTO)).map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(final Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId).map(customer -> {
            customer.setCustomerName(customerDTO.getCustomerName());
            return customer;
        }).flatMap(customerRepository::save).map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> patchCustomer(final Integer customerId, CustomerDTO customerDTO) {
        return customerRepository.findById(customerId).map(customer -> {
            if (StringUtils.hasText(customerDTO.getCustomerName()))
                customer.setCustomerName(customerDTO.getCustomerName());
            return customer;
        }).flatMap(customerRepository::save).map(customerMapper::customerToCustomerDTO);
    }

    @Override
    public Mono<Void> deleteCustomerById(final Integer customerId) {
        return customerRepository.deleteById(customerId);
    }
}
