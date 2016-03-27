package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.Routes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends CrudRepository<Routes, Long> {
}
