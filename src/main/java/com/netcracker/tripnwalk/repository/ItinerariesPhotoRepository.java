package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.ItinerariesPhoto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItinerariesPhotoRepository extends CrudRepository<ItinerariesPhoto, Long> {}
