package com.omsai.restservice.repositories;

import com.omsai.restservice.entities.Beer;
import com.omsai.restservice.model.BeerStyle;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {

        Beer savedBeer = beerRepository.save(Beer.builder()
                .beerName("My Beer")
                .beerStyle(BeerStyle.PALE_ALE)
                .upc("1234")
                .price(new BigDecimal("11.50"))
                .build());
        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerNameTooLong() {

        assertThrows(ConstraintViolationException.class, () -> {
            beerRepository.save(Beer.builder()
                    .beerName("My Beer higgytddfuytfdrdufltdfdufiydttfyxsrtdufytdtfufygfgdrydkdtsydulifi")
                    .beerStyle(BeerStyle.PALE_ALE)
                    .upc("1234")
                    .price(new BigDecimal("11.50"))
                    .build());
            beerRepository.flush();
        });
    }
}