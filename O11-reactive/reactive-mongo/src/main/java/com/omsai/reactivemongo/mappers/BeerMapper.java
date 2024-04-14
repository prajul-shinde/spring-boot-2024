package com.omsai.reactivemongo.mappers;

import com.omsai.reactivemongo.domain.Beer;
import com.omsai.reactivemongo.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDtoToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDto(Beer beer);
}
