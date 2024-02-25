package com.omsai.restservice.bootstrap;

import com.omsai.restservice.repositories.BeerRepository;
import com.omsai.restservice.repositories.CustomerRepository;
import com.omsai.restservice.services.BeerCsvService;
import com.omsai.restservice.services.BeerCsvServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerCsvService beerCsvService;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {

        bootstrapData = new BootstrapData(beerRepository, customerRepository, beerCsvService);
    }

    @Test
    void testRun() throws Exception {

        bootstrapData.run();

        assertThat(customerRepository.count()).isEqualTo(3);
        assertThat(beerRepository.count()).isEqualTo(2413);
    }

}