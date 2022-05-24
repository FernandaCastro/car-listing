package com.fcastro.carlisting;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public class ListingServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer POSTGRES_CONTAINER = new PostgreSQLContainer(PostgreSQLContainer.IMAGE)
            .withDatabaseName("car_listing_IT")
            .withUsername("db")
            .withPassword("db");

    @Autowired
    private ListingService service;

    @Autowired
    private ListingRepository repository;

    @AfterEach
    private void reset() {
        repository.deleteAll();
    }

    @Test
    public void givenExistingListing_whenSave_shouldUpdateExistingListing() {
        //given
        Listing existingListing = Listing.builder()
                .dealerId(1L)
                .code("a")
                .make("mercedes")
                .model("a")
                .kw(123)
                .year(2014)
                .color("black")
                .price(BigDecimal.valueOf(15950L))
                .build();
        existingListing = repository.save(existingListing);

        Listing updatedListing = Listing.builder()
                .dealerId(1L)
                .code("a")
                .make("mercedes")
                .model("a")
                .kw(123)
                .year(2014)
                .color("red")
                .price(BigDecimal.valueOf(15950L))
                .build();

        //when
        updatedListing = service.save(updatedListing);

        //then
        assertThat(updatedListing).isNotNull();
        assertThat(updatedListing.getId()).isEqualTo(existingListing.getId());
        assertThat(updatedListing.getColor()).isIn("red");
    }

    @Test
    public void givenExistingListings_whenSaveAll_shouldUpdateExistingListings() {
        //given
        List<Listing> existingListings = new ArrayList<>();
        existingListings.add(Listing.builder()
                .dealerId(1L)
                .code("a")
                .make("mercedes")
                .model("a")
                .kw(123)
                .year(2014)
                .color("black")
                .price(BigDecimal.valueOf(15950L))
                .build());
        existingListings.add(Listing.builder()
                .dealerId(1L)
                .code("b")
                .make("audi")
                .model("a3")
                .kw(130)
                .year(2018)
                .color("gray")
                .price(BigDecimal.valueOf(20950L))
                .build());
        existingListings = repository.saveAll(existingListings);

        List<Listing> updatedListings = new ArrayList<>();
        updatedListings.add(Listing.builder()
                .code("a")
                .make("mercedes")
                .model("a")
                .kw(123)
                .year(2015)
                .color("red")
                .price(BigDecimal.valueOf(15950L))
                .build());
        updatedListings.add(Listing.builder()
                .code("b")
                .make("audi")
                .model("a3")
                .kw(130)
                .year(2019)
                .color("gray")
                .price(BigDecimal.valueOf(20950L))
                .build());

        Long dealerId = 1L;

        //when
        updatedListings = service.saveAll(dealerId, updatedListings);

        //then
        assertThat(updatedListings).isNotNull();
        assertThat(updatedListings).hasSize(2);

        assertThat(updatedListings.get(0).getId()).isEqualTo(existingListings.get(0).getId());
        assertThat(updatedListings.get(0).getDealerId()).isEqualTo(dealerId);
        assertThat(updatedListings.get(0).getYear()).isEqualTo(2015);

        assertThat(updatedListings.get(1).getId()).isEqualTo(existingListings.get(1).getId());
        assertThat(updatedListings.get(1).getDealerId()).isEqualTo(dealerId);
        assertThat(updatedListings.get(1).getYear()).isEqualTo(2019);
    }

    @Test
    public void givenNewListing_whenSaveAll_shouldAddListings() {
        //given
        List<Listing> newListings = new ArrayList<>();
        newListings.add(Listing.builder()
                .code("a")
                .make("mercedes")
                .model("a")
                .kw(123)
                .year(2015)
                .color("red")
                .price(BigDecimal.valueOf(15950L))
                .build());
        newListings.add(Listing.builder()
                .code("b")
                .make("audi")
                .model("a3")
                .kw(130)
                .year(2019)
                .color("gray")
                .price(BigDecimal.valueOf(20950L))
                .build());

        Long dealerId = 1L;

        //when
        newListings = service.saveAll(dealerId, newListings);

        //then
        assertThat(newListings).isNotNull();
        assertThat(newListings).hasSize(2);

        assertThat(newListings.get(0).getId()).isNotNull();
        assertThat(newListings.get(0).getDealerId()).isEqualTo(dealerId);

        assertThat(newListings.get(1).getId()).isNotNull();
        assertThat(newListings.get(1).getDealerId()).isEqualTo(dealerId);
    }
}
