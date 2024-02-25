package com.omsai.restservice.services;

import com.omsai.restservice.model.BeerCSVRecord;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BeerCsvServiceImplTest {

    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCsv() throws FileNotFoundException {

        File csvFile = ResourceUtils.getFile("classpath:csvdata/beers.csv");
        List<BeerCSVRecord> beerCSVRecords = beerCsvService.convertCsv(csvFile);
        System.out.println(beerCSVRecords.size());
        assertThat(beerCSVRecords.size()).isGreaterThan(0);
    }
}