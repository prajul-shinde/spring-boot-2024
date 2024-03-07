package com.omsai.restservice.services;

import com.omsai.restservice.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

public interface BeerCsvService {

    List<BeerCSVRecord> convertCsv(File csvFile);
}
