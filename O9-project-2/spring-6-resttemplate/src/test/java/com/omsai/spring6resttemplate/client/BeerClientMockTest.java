package com.omsai.spring6resttemplate.client;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omsai.spring6resttemplate.config.RestTemplateBuilderConfig;
import com.omsai.spring6resttemplate.model.BeerDTO;
import com.omsai.spring6resttemplate.model.BeerDTOPageImpl;
import com.omsai.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(BeerClientImpl.class)
@Import({RestTemplateBuilderConfig.class})
public class BeerClientMockTest {

    private static final String URL = "http://localhost:8080";

    private BeerClient beerClient;

    private MockRestServiceServer server;

    @Mock
    private RestTemplateBuilder mocRestTemplateBuilder = new RestTemplateBuilder(new MockServerRestTemplateCustomizer());

    BeerDTO dto;

    String dtoJson;

    @BeforeEach
    void setUp() throws JsonProcessingException {

        RestTemplate restTemplate = restTemplateBuilderConfigured.build();
        server = MockRestServiceServer.bindTo(restTemplate).build();
        when(mocRestTemplateBuilder.build()).thenReturn(restTemplate);
        beerClient = new BeerClientImpl(mocRestTemplateBuilder);

        dto = getBeerDTO();
        dtoJson = objectMapper.writeValueAsString(dto);
    }

    @Autowired
    private RestTemplateBuilder restTemplateBuilderConfigured;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testDeleteBeer() {

        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, dto.getId()))
                .andRespond(withNoContent());
        beerClient.deleteBeer(dto.getId());

        server.verify();
    }

    @Test
    void testDeleteNotFound() {

        server.expect(method(HttpMethod.DELETE))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH, dto.getId()))
                .andRespond(withResourceNotFound());

        assertThrows(HttpClientErrorException.class, () -> {
            beerClient.deleteBeer(dto.getId());
        });
    }

    @Test
    void testUpdateBeer() {

        server.expect(method(HttpMethod.PUT))
                .andExpect(requestToUriTemplate(URL + BeerClientImpl.GET_BEER_BY_ID_PATH,
                        dto.getId()))
                .andRespond(withNoContent());

        mockGetOperation();

        BeerDTO responseDto = beerClient.updateBeer(dto);
        assertThat(responseDto.getId()).isEqualTo(dto.getId());
    }

    @Test
    void testCreateBeer() {

        URI uri = UriComponentsBuilder.fromPath("/api/v1/beers/{beerId}").build(dto.getId());
        server.expect(method(HttpMethod.POST))
                .andExpect(requestTo(URL + "/api/v1/beers"))
                .andRespond(withAccepted().location(uri));
        mockGetOperation();
        BeerDTO beerById = beerClient.createBeer(dto);
        assertThat(beerById.getId()).isEqualTo(dto.getId());
    }

    @Test
    void testGetBeerById() {

        mockGetOperation();
        BeerDTO beerById = beerClient.getBeerById(dto.getId());
        assertThat(beerById.getId()).isEqualTo(dto.getId());
    }

    private void mockGetOperation() {
        server.expect(method(HttpMethod.GET))
                .andExpect(requestToUriTemplate(URL + "/api/v1/beers/{beerId}", dto.getId()))
                .andRespond(withSuccess(dtoJson, MediaType.APPLICATION_JSON));
    }

    @Test
    void testListBeers() throws JsonProcessingException {

        String payload = objectMapper.writeValueAsString(getBeerDTOPage());
        server.expect(method(HttpMethod.GET))
                .andExpect(requestTo(URL + "/api/v1/beers"))
                .andRespond(withSuccess(payload, MediaType.APPLICATION_JSON));

        Page<BeerDTO> beerDTOS = beerClient.listBeers();
        assertThat(beerDTOS.getContent().size()).isGreaterThan(0);
    }

    BeerDTO getBeerDTO() {

        return BeerDTO.builder()
                .id(UUID.randomUUID())
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(500)
                .upc("12345")
                .build();
    }

    BeerDTOPageImpl getBeerDTOPage() {

        return new BeerDTOPageImpl(List.of(getBeerDTO()), 1, 25, 1);
    }
}
