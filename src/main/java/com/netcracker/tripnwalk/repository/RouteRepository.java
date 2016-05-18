package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.Route;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteRepository extends CrudRepository<Route, Long> {

    @Query("select r from Route r where r.id not in :ids order by rand()")
    List<Route> getTopList(@Param("ids") List<Long> routeId, Pageable pageable);

    @Query("select count(r) from Route r")
    Long getCointRow();
}
