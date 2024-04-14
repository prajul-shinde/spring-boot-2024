package com.omsai.reactivemongo.controllers;


import com.omsai.reactivemongo.model.BeerDTO;
import com.omsai.reactivemongo.sevices.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {

    private final BeerService beerService;
    public static final String BEER_PATH = "/api/v2/beer";
    public static final String BEER_PATH_ID = BEER_PATH + "/{beerId}";

    @GetMapping(BEER_PATH)
    public Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_PATH_ID)
    public Mono<BeerDTO> getById(@PathVariable("id") final String id) {

        return beerService.getById(id).switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(BEER_PATH)
    public Mono<ResponseEntity<BeerDTO>> saveBeer(@Validated @RequestBody BeerDTO beerDTO) {

        return beerService.saveNewBeer(beerDTO).map(savedBeer -> ResponseEntity.created(UriComponentsBuilder.fromHttpUrl("http:localhost:8080" + BEER_PATH + "/" + savedBeer.getId()).build().toUri()).build());
    }

    @PutMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<BeerDTO>> updateBeer(@PathVariable("id") final String id, @Validated @RequestBody BeerDTO beerDTO) {
        return beerService.updateBeer(id, beerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(updatedBeer -> ResponseEntity.noContent().build());
    }

    @PatchMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<BeerDTO>> patchBeer(@PathVariable("id") String id, @Validated @RequestBody BeerDTO beerDTO) {

        return beerService.patchBeer(id, beerDTO)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(patchedBeer -> ResponseEntity.noContent().build());
    }

    @DeleteMapping(BEER_PATH_ID)
    public Mono<ResponseEntity<Void>> deleteById(@PathVariable("id") String id) {

        return beerService.getById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .map(beerDTO -> beerService.deleteById(beerDTO.getId()))
                .thenReturn(ResponseEntity.noContent().build());
    }
}
