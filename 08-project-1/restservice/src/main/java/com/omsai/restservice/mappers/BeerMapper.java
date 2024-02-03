package com.omsai.restservice.mappers;

import com.omsai.restservice.entities.Beer;
import com.omsai.restservice.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

    Beer beerDTOToBeer(BeerDTO beerDTO);

    BeerDTO beerToBeerDTO(Beer beer);
}
