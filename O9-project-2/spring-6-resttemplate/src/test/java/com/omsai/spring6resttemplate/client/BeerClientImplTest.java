package com.omsai.spring6resttemplate.client;

import com.omsai.spring6resttemplate.model.BeerDTO;
import com.omsai.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void testDeleteBeer() {

        BeerDTO newDTO = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("12345")
                .build();
        BeerDTO beerDTO = beerClient.createBeer(newDTO);

        beerClient.deleteBeer(beerDTO.getId());
        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.getBeerById(beerDTO.getId());
        });
    }

    @Test
    void testUpdateBeer() {

        BeerDTO newDTO = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("12345")
                .build();
        BeerDTO beerDTO = beerClient.createBeer(newDTO);

        final String newName = "Mango Bobs 3";
        beerDTO.setBeerName(newName);
        BeerDTO updatedDTO = beerClient.updateBeer(beerDTO);
        assertNotNull(updatedDTO);
    }

    @Test
    void testCreateBeer() {

        BeerDTO newDTO = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("12345")
                .build();

        BeerDTO savedDTO = beerClient.createBeer(newDTO);
        assertNotNull(savedDTO);
    }

    @Test
    void testGetBeerById() {
        BeerDTO beerDTO = beerClient.listBeers().getContent().getFirst();
        BeerDTO byId = beerClient.getBeerById(beerDTO.getId());
        assertNotNull(byId);
    }

    @Test
    void testListBeersNoBeerName() {
        beerClient.listBeers(null, null, null, null, null);
    }

    @Test
    void testListBeers() {
        beerClient.listBeers("ALE", null, null, null, null);
    }
}