package com.omsai.restservice.mappers;

import com.omsai.restservice.entities.Customer;
import com.omsai.restservice.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);
}
