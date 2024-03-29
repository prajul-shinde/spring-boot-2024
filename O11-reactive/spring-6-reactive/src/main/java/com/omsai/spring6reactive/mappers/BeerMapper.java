package com.omsai.spring6reactive.mappers;

import com.omsai.spring6reactive.domain.Beer;
import com.omsai.spring6reactive.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDTOToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDTO(Beer beer);
}
