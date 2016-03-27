package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.RouteData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteDataRepository extends CrudRepository<RouteData, Long> {
}
