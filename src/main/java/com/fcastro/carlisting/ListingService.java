package com.fcastro.carlisting;

import lombok.AllArgsConstructor;
import org.hibernate.hql.internal.ast.tree.Statement;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.io.BufferedReader;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ListingService {

    private final ListingRepository repository;
    private final ListingCSVReader csvReader;

    public List<Listing> findAllByAllParams(MultiValueMap<String, String> allParams) {
        return repository.findAllByAllParams(allParams);
    }

    public List<Listing> findAll() {
        return repository.findAll();
    }

    public Listing save(Listing listing) {

        Listing savedListing = null;

        var listingOption = repository.findByDealerIdAndCode(listing.getDealerId(), listing.getCode());
        if (listingOption.isPresent()) {
            listing.setId(listingOption.get().getId());
        }
        savedListing = repository.save(listing);

        return savedListing;
    }

    public List<Listing> processFile(Long dealerId, BufferedReader reader) {
        List<Listing> listings = csvReader.readFile(reader);
        return saveAll(dealerId, listings);
    }

    public List<Listing> saveAll(Long dealerId, List<Listing> listings) {

        List<Listing> savedListings = new ArrayList<>();
        if (listings == null) {
            return savedListings;
        }

        listings.forEach(listing -> {
            listing.setDealerId(dealerId);
            savedListings.add(save(listing));
        });

        return savedListings;
    }
}
