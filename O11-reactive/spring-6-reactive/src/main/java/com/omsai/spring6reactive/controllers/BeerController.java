package com.omsai.spring6reactive.controllers;

import com.omsai.spring6reactive.model.BeerDTO;
import com.omsai.spring6reactive.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {

    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";
    private final BeerService beerService;

    @DeleteMapping(BEER_PATH_ID)
    Mono<ResponseEntity<Void>> deleteBeerById(@PathVariable("beerId") Integer beerId) {

        return beerService.deleteById(beerId).map(response -> ResponseEntity.noContent().build());
    }

    @PatchMapping(BEER_PATH_ID)
    Mono<ResponseEntity<BeerDTO>> patchBeerById(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDTO beerDTO) {

        return beerService.patchBeer(beerId, beerDTO).map(patchedDTO ->
                ResponseEntity.ok().build());
    }

    @PutMapping(BEER_PATH_ID)
    Mono<ResponseEntity<BeerDTO>> updateExistingBeer(@PathVariable("beerId") Integer beerId, @Validated @RequestBody BeerDTO beerDTO) {

        return beerService.updateBeer(beerId, beerDTO).map(savedDTO ->
                ResponseEntity.ok().build());

    }

    @PostMapping(BEER_PATH)
    Mono<ResponseEntity<BeerDTO>> createNewBeer(@Validated @RequestBody BeerDTO beerDTO) {

        return beerService.saveNewBeer(beerDTO).map(savedDTO ->
                ResponseEntity.created(UriComponentsBuilder
                                .fromHttpUrl("http://localhost:8080" + BEER_PATH + "/" + savedDTO.getId())
                                .build()
                                .toUri())
                        .build());

    }

    @GetMapping(BEER_PATH_ID)
    Mono<BeerDTO> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId);
    }

    @GetMapping(BEER_PATH)
    Flux<BeerDTO> listBeers() {

        return beerService.listBeers();
    }
}