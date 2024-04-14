package com.omsai.reactivemongo.sevices;

import com.omsai.reactivemongo.mappers.BeerMapper;
import com.omsai.reactivemongo.model.BeerDTO;
import com.omsai.reactivemongo.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll().map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> saveNewBeer(BeerDTO beerDTO) {
        return beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> getById(final String id) {

        return beerRepository.findById(id).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> updateBeer(String id, BeerDTO beerDTO) {
        return beerRepository.findById(id).map(foundBeer -> {
            foundBeer.setBeerName(beerDTO.getBeerName());
            foundBeer.setBeerStyle(beerDTO.getBeerStyle());
            foundBeer.setPrice(beerDTO.getPrice());
            foundBeer.setUpc(beerDTO.getUpc());
            foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            return foundBeer;
        }).flatMap(beerRepository::save).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<BeerDTO> patchBeer(String id, BeerDTO beerDTO) {
        return beerRepository.findById(id).map(foundBeer -> {

            if (StringUtils.hasText(beerDTO.getBeerName()))
                foundBeer.setBeerName(beerDTO.getBeerName());
            if (StringUtils.hasText(beerDTO.getBeerStyle()))
                foundBeer.setBeerStyle(beerDTO.getBeerStyle());
            if (beerDTO.getPrice() != null)
                foundBeer.setPrice(beerDTO.getPrice());
            if (StringUtils.hasText(beerDTO.getUpc()))
                foundBeer.setUpc(beerDTO.getUpc());
            if (beerDTO.getQuantityOnHand() != null)
                foundBeer.setQuantityOnHand(beerDTO.getQuantityOnHand());

            return foundBeer;
        }).flatMap(beerRepository::save).map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return beerRepository.deleteById(id);
    }

    @Override
    public Mono<BeerDTO> findFirstByBeerName(String beerName) {
        return beerRepository.findFirstByBeerName(beerName)
                .map(beerMapper::beerToBeerDTO);
    }

    @Override
    public Flux<BeerDTO> findByBeerStyle(String beerStyle) {
        return beerRepository.findByBeerStyle(beerStyle)
                .map(beerMapper::beerToBeerDTO);
    }
}
