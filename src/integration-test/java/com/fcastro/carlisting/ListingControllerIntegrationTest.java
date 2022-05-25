package com.fcastro.carlisting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Testcontainers
public class ListingControllerIntegrationTest {

    @Container
    private static final PostgreSQLContainer POSTGRES_CONTAINER = new PostgreSQLContainer(PostgreSQLContainer.IMAGE)
            .withDatabaseName("car_listing_IT")
            .withUsername("db")
            .withPassword("db");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ListingRepository listingRepository;

    @AfterEach
    private void reset(){
        listingRepository.deleteAll();
    }

    @Test
    public void givenParamYear_whenSearch_returnListings() throws Exception {
        //given
        Listing listing1 = Listing.builder().dealerId(1L).code("a").make("mercedes").model("a").kw(123).year(2014).color("black").price(BigDecimal.valueOf(15950L)).build();
        Listing listing2 = Listing.builder().dealerId(1L).code("b").make("audi").model("a3").kw(111).year(2015).color("white").price(BigDecimal.valueOf(17210L)).build();
        Listing listing3 = Listing.builder().dealerId(1L).code("c").make("vw").model("golf").kw(86).year(2015).color("green").price(BigDecimal.valueOf(14980L)).build();
        listingRepository.saveAll(Arrays.asList(listing1, listing2, listing3));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("year", Arrays.asList("2015"));

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.get("/car-listings/")
                        .params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$"), hasSize(2)));
    }
}
