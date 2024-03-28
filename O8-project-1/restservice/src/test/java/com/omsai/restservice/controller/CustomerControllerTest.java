package com.omsai.restservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omsai.restservice.config.SpringSecConfig;
import com.omsai.restservice.model.CustomerDTO;
import com.omsai.restservice.services.CustomerService;
import com.omsai.restservice.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.omsai.restservice.controller.CustomerController.CUSTOMER_PATH;
import static com.omsai.restservice.controller.CustomerController.CUSTOMER_PATH_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
@Import(SpringSecConfig.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDTO> customerArgumentCaptor;

    public static final SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor JWT_REQUEST_POST_PROCESSOR = jwt().jwt(jwt -> {
        jwt.claims(claims -> {
                    claims.put("scope", "message-read");
                    claims.put("scope", "message-write");
                })
                .subject("messaging-client")
                .notBefore(Instant.now().minusSeconds(5l));
    });

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void listCustomers() throws Exception {

        given(customerService.listCustomers()).willReturn(customerServiceImpl.listCustomers());

        mockMvc.perform(get(CUSTOMER_PATH)
                        .with(JWT_REQUEST_POST_PROCESSOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getCustomerById() throws Exception {

        CustomerDTO testCustomer = customerServiceImpl.listCustomers().getFirst();
        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CUSTOMER_PATH_ID, testCustomer.getId())
                        .with(JWT_REQUEST_POST_PROCESSOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.customerName", is(testCustomer.getCustomerName())));
    }

    @Test
    void testGetCustomerByIdNotFound() throws Exception {

        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CUSTOMER_PATH_ID, UUID.randomUUID())
                        .with(JWT_REQUEST_POST_PROCESSOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateNewCustomer() throws Exception {

        CustomerDTO customer = customerServiceImpl.listCustomers().getFirst();
        given(customerService.saveNewCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.listCustomers().get(1));

        mockMvc.perform(post(CUSTOMER_PATH)
                        .with(JWT_REQUEST_POST_PROCESSOR)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));

    }

    @Test
    void testUpdateCustomer() throws Exception {

        CustomerDTO customer = customerServiceImpl.listCustomers().getFirst();

        given(customerService.updateCustomerById(any(), any())).willReturn(Optional.of(customer));
        mockMvc.perform(put(CUSTOMER_PATH_ID, customer.getId())
                        .with(JWT_REQUEST_POST_PROCESSOR)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    void testDeleteCustomer() throws Exception {

        CustomerDTO customer = customerServiceImpl.listCustomers().getFirst();
        given(customerService.deleteCustomerById(any())).willReturn(true);
        mockMvc.perform(delete(CUSTOMER_PATH_ID, customer.getId())
                        .with(JWT_REQUEST_POST_PROCESSOR)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void testPatchCustomer() throws Exception {

        CustomerDTO customer = customerServiceImpl.listCustomers().getFirst();

        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Customer");

        given(customerService.patchCustomerById(any(), any())).willReturn(Optional.of(customer));
        mockMvc.perform(patch(CUSTOMER_PATH_ID, customer.getId())
                        .with(JWT_REQUEST_POST_PROCESSOR)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerMap)))
                .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }
}