package com.omsai.reactivemongo.sevices;

import com.omsai.reactivemongo.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO);

    Mono<BeerDTO> getById(final String id);

    Mono<BeerDTO> updateBeer(final String id, BeerDTO beerDTO);

    Mono<BeerDTO> patchBeer(final String id, BeerDTO beerDTO);

    Mono<Void> deleteById(final String id);

    Mono<BeerDTO> findFirstByBeerName(String beerName);

    Flux<BeerDTO> findByBeerStyle(String beerStyle);


}
