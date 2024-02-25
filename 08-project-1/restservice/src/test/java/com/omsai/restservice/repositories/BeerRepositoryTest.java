package com.omsai.restservice.repositories;

import com.omsai.restservice.bootstrap.BootstrapData;
import com.omsai.restservice.entities.Beer;
import com.omsai.restservice.model.BeerStyle;
import com.omsai.restservice.services.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testListBeersByName() {

        //wildcard string contains ipa
        List<Beer> beerList = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%");
        assertThat(beerList.size()).isEqualTo(336);
    }

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