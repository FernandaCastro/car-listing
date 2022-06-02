package com.fcastro.carlisting;

import org.springframework.util.MultiValueMap;

import java.util.List;

public interface ListingRepositoryCustom {

    List<Listing> findAllByAllParams(MultiValueMap<String, String> allParams);
}
