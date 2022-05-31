package com.fcastro.carlisting;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;


@Component
public class ListingCSVReader {

    protected List<Listing> readFile(BufferedReader reader) {

        CsvToBean<ListingCSV> csvToBean = new CsvToBeanBuilder<ListingCSV>(reader)
                .withType(ListingCSV.class)
                .withSeparator(',')
                .build();

        try {
            List<ListingCSV> listingCSVs = csvToBean.parse();
            return mapToListing(listingCSVs);
        }catch(RuntimeException e){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, e.getCause().getMessage() + " - " + e.getMessage());
        }
    }

    private List<Listing> mapToListing(List<ListingCSV> listingCSVs) {
        List<Listing> listings = new ArrayList<>();
        listingCSVs.forEach(listingCSV ->
                listings.add(Listing.builder()
                        .code(listingCSV.getCode())
                        .make(listingCSV.getMakeModel() == null ? null : listingCSV.getMakeModel().get(0))
                        .model(listingCSV.getMakeModel()== null ? null : listingCSV.getMakeModel().get(1))
                        .kw(listingCSV.getKw())
                        .year(listingCSV.getYear())
                        .color(listingCSV.getColor())
                        .price(listingCSV.getPrice())
                        .build()));
        return listings;
    }
}
