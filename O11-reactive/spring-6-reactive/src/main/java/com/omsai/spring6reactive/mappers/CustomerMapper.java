package com.omsai.spring6reactive.mappers;

import com.omsai.spring6reactive.domain.Customer;
import com.omsai.spring6reactive.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);
}
