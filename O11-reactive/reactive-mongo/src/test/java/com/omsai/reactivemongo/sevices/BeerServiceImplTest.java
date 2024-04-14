package com.omsai.reactivemongo.sevices;

import com.omsai.reactivemongo.domain.Beer;
import com.omsai.reactivemongo.mappers.BeerMapper;
import com.omsai.reactivemongo.model.BeerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    private BeerService beerService;

    @Autowired
    private BeerMapper beerMapper;

    BeerDTO beerDTO;

    @BeforeEach
    void setUp() {
        beerDTO = beerMapper.beerToBeerDTO(getTestBeer());
    }

    @Test
    void testSaveBeer() {

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();

        Mono<BeerDTO> savedMono = beerService.saveNewBeer(beerDTO);
        savedMono.subscribe(savedDTO -> {
            System.out.println(savedDTO.getId());
            atomicBoolean.set(true);
            atomicDto.set(savedDTO);
        });
        await().untilTrue(atomicBoolean);

        BeerDTO persistedDto = atomicDto.get();
        assertThat(persistedDto).isNotNull();
        assertThat(persistedDto.getId()).isNotNull();
    }

    @Test
    void testUpdateBeer() {
        final String newName = "new beer name";
        AtomicReference<BeerDTO> atomicDto = new AtomicReference<>();
        beerService.saveNewBeer(beerDTO)
                .map(savedBeerDto -> {
                    savedBeerDto.setBeerName(newName);
                    return savedBeerDto;
                })
                .flatMap(beerService::saveNewBeer) // save updated beer
                .flatMap(savedUpdatedDto -> beerService.getById(savedUpdatedDto.getId())) // get from db
                .subscribe(dtoFromDb -> {
                    atomicDto.set(dtoFromDb);
                });
        await().until(() -> atomicDto.get() != null);
        assertThat(atomicDto.get().getBeerName()).isEqualTo(newName);
    }

    @Test
    void testDeleteBeer() {
        BeerDTO beerToDelete = getSavedBeerDto();

        beerService.deleteById(beerToDelete.getId()).block();

        Mono<BeerDTO> expectedEmptyBeerMono = beerService.getById(beerToDelete.getId());

        BeerDTO emptyBeer = expectedEmptyBeerMono.block();

        assertThat(emptyBeer).isNull();

    }

    @Test
    void testFindFirstByBeerName() {
        BeerDTO savedBeerDto = getSavedBeerDto();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        Mono<BeerDTO> foundDto = beerService.findFirstByBeerName(savedBeerDto.getBeerName());
        foundDto.subscribe(dto -> {
            atomicBoolean.set(true);
            System.out.println(dto.toString());
        });
        await().untilTrue(atomicBoolean);
    }

    @Test
    void testFindByBeerStyle() {
        BeerDTO beerDto = getSavedBeerDto();
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        beerService.findByBeerStyle(beerDto.getBeerStyle())
                .subscribe(dto -> {
                    atomicBoolean.set(true);
                });
        await().untilTrue(atomicBoolean);
    }

    public BeerDTO getSavedBeerDto() {
        return beerService.saveNewBeer(beerDTO).block();
    }

    public static Beer getTestBeer() {

        return Beer.builder()
                .beerName("Space Dust")
                .beerStyle("IPA")
                .price(BigDecimal.TEN)
                .quantityOnHand(12)
                .upc("123123")
                .build();
    }
}