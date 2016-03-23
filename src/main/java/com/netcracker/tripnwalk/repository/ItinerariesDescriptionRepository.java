package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.ItinerariesDescription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItinerariesDescriptionRepository extends CrudRepository<ItinerariesDescription, Long> {
}
