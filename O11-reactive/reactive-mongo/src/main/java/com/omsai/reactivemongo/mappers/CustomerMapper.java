package com.omsai.reactivemongo.mappers;

import com.omsai.reactivemongo.domain.Customer;
import com.omsai.reactivemongo.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);
}
