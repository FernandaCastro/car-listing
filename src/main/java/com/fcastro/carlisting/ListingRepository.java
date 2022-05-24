package com.fcastro.carlisting;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListingRepository extends JpaRepository<Listing, Long>, ListingRepositoryCustom {

    Optional<Listing> findByDealerIdAndCode(Long dealerId, String code);
}
