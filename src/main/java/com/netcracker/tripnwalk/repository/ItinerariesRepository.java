package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.Itineraries;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItinerariesRepository extends CrudRepository<Itineraries, Long> {
}
