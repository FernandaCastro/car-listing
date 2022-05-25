package com.fcastro.carlisting;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
public class ListingRepositoryIntegrationTest {

    @Container
    private static final PostgreSQLContainer POSTGRES_CONTAINER = new PostgreSQLContainer(PostgreSQLContainer.IMAGE)
            .withDatabaseName("car_listing_IT")
            .withUsername("db")
            .withPassword("db");

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ListingRepository listingRepository;

    @Test
    public void givenParamColorGreenAndWhite_whenFindAllByAllParams_returnFilteredListings(){
        //given
        Listing listing1 = Listing.builder().dealerId(1L).code("a").make("mercedes").model("a").kw(123).year(2014).color("black").price(BigDecimal.valueOf(15950L)).build();
        Listing listing2 = Listing.builder().dealerId(1L).code("b").make("audi").model("a3").kw(111).year(2016).color("white").price(BigDecimal.valueOf(17210L)).build();
        Listing listing3 = Listing.builder().dealerId(1L).code("c").make("vw").model("golf").kw(86).year(2018).color("green").price(BigDecimal.valueOf(14980L)).build();
        Arrays.asList(listing1, listing2, listing3).forEach(entityManager::persistAndFlush);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("color", Arrays.asList("green", "white"));

        //when
        List<Listing> result = listingRepository.findAllByAllParams(params);

        //then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result).hasSize(2);
        Assertions.assertThat(result.get(0).getColor()).isIn("green", "white");
        Assertions.assertThat(result.get(1).getColor()).isIn("green", "white");
    }
}
