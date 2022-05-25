package com.fcastro.carlisting;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@RestController
@AllArgsConstructor
public class ListingController {

    private final ListingService service;

    @PostMapping(value = "/upload_csv/{dealerId}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<List<Listing>> upload_csv(@PathVariable Long dealerId, @RequestParam MultipartFile file){

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "ISO-8859-1"));
            return ResponseEntity.ok(service.processFile(dealerId, reader));

        } catch (IOException e) {
            throw new IllegalStateException("File [" + file.getName() + "] could not be read.");
        }
    }

    @PostMapping("/car-listings")
    public ResponseEntity<Listing> create(@RequestBody Listing listing){
        return ResponseEntity.ok(service.save(listing));
    }

    @GetMapping("/car-listings")
    public ResponseEntity<List<Listing>> search(@RequestParam(required = false) MultiValueMap<String, String> allParams){

        List<Listing> listings;

        if (allParams != null && !allParams.isEmpty()){
            listings = service.findAllByAllParams(allParams);
        }else{
            listings = service.findAll();
        }

        if (listings == null || listings.isEmpty()){
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(listings);
    }
}