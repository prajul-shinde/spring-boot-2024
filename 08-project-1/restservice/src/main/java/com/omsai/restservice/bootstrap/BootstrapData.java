package com.omsai.restservice.bootstrap;

import com.omsai.restservice.entities.Beer;
import com.omsai.restservice.entities.Customer;
import com.omsai.restservice.model.BeerStyle;
import com.omsai.restservice.repositories.BeerRepository;
import com.omsai.restservice.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;

    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadCustomerData() {

        if (customerRepository.count() == 0) {

            Customer customer1 = Customer.builder().customerName("Prajul").createdDate(LocalDateTime.now()).lastModifiedDate(LocalDateTime.now()).build();

            Customer customer2 = Customer.builder().customerName("Onkar").createdDate(LocalDateTime.now()).lastModifiedDate(LocalDateTime.now()).build();

            Customer customer3 = Customer.builder().customerName("Ganesh").createdDate(LocalDateTime.now()).lastModifiedDate(LocalDateTime.now()).build();

            customerRepository.saveAll(List.of(customer1, customer2, customer3));
        }
    }

    private void loadBeerData() {

        if (beerRepository.count() == 0) {

            Beer beer1 = Beer.builder().beerName("Galaxy Cat").beerStyle(BeerStyle.PALE_ALE).upc("12356").price(new BigDecimal("12.99")).quantityOnHand(122).createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

            Beer beer2 = Beer.builder().beerName("Crank").beerStyle(BeerStyle.PALE_ALE).upc("12356222").price(new BigDecimal("11.99")).quantityOnHand(392).createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

            Beer beer3 = Beer.builder().beerName("Sunshine City").beerStyle(BeerStyle.IPA).upc("12356").price(new BigDecimal("13.99")).quantityOnHand(144).createdDate(LocalDateTime.now()).updateDate(LocalDateTime.now()).build();

            beerRepository.save(beer1);
            beerRepository.save(beer2);
            beerRepository.save(beer3);
        }
    }
}
