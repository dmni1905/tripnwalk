package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.Itineraries;
import com.netcracker.tripnwalk.entry.ItinerariesPoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItinerariesPointRepository extends CrudRepository<ItinerariesPoint, Long> {
}
