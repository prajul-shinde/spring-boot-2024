package com.omsai.restservice.controller;

import com.omsai.restservice.entities.Beer;
import com.omsai.restservice.mappers.BeerMapper;
import com.omsai.restservice.model.BeerDTO;
import com.omsai.restservice.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BeerControllerIntegrationTest {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Test
    void testListBeers() {

        List<BeerDTO> dtos = beerController.listBeers();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {

        beerRepository.deleteAll();
        List<BeerDTO> dtos = beerController.listBeers();

        assertThat(dtos.size()).isEqualTo(0);
    }

    @Test
    void testGetBeerById() {

        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO dto = beerController.getBeerById(beer.getId());

        assertThat(dto).isNotNull();
    }

    @Test
    void testGetBeerByIdNotFound() {

        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void testSaveNewBeer() {

        BeerDTO beerDTO = BeerDTO.builder().beerName("New Beer").build();
        ResponseEntity responseEntity = beerController.handlePost(beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(location[4]);
        Beer savedBeer = beerRepository.findById(savedUUID).get();
        assertThat(savedBeer).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateBeer() {

        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        beerDTO.setBeerName("Updated");
        ResponseEntity responseEntity = beerController.updateBeerById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Beer updatedBear = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBear.getBeerName()).isEqualTo("Updated");
    }

    @Test
    void testUpdateBeerNotFound() {

        assertThrows(NotFoundException.class, () -> {
            beerController.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteByIdFound() {

        Beer beer = beerRepository.findAll().getFirst();
        ResponseEntity responseEntity = beerController.deleteBeerById(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(beerRepository.findById(beer.getId())).isEmpty();

    }

    @Test
    void testDeleteByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            beerController.deleteBeerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testPatchBeerById() {

        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDTO = beerMapper.beerToBeerDTO(beer);
        beerDTO.setId(null);
        beerDTO.setVersion(null);
        beerDTO.setBeerName("Patched");
        ResponseEntity responseEntity = beerController.patchBeerById(beer.getId(), beerDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        Beer updatedBear = beerRepository.findById(beer.getId()).get();
        assertThat(updatedBear.getBeerName()).isEqualTo("Patched");
    }

    @Test
    void testPatchBeerByIdNotFound() {

        assertThrows(NotFoundException.class, () -> {
            beerController.patchBeerById(UUID.randomUUID(), BeerDTO.builder().build());
        });
    }

}