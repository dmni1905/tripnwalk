package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.Route;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {
}
