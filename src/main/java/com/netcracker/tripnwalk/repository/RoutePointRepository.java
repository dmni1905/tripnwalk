package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.RoutePoint;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutePointRepository extends CrudRepository<RoutePoint, Long> {
}
