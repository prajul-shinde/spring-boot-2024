package com.omsai.spring6resttemplate.client;

import com.omsai.spring6resttemplate.model.BeerDTO;
import com.omsai.spring6resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {

    Page<BeerDTO> listBeers();

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Boolean showInventory, Integer pageNumber, Integer pageSize);

    BeerDTO getBeerById(UUID beerId);

    BeerDTO createBeer(BeerDTO newDTO);

    BeerDTO updateBeer(BeerDTO beerDTO);

    void deleteBeer(UUID beerId);
}
