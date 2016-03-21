package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.AppUser;
import com.netcracker.tripnwalk.entry.Itineraries;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<AppUser, Long> {
    @Query("select u from AppUser u where u.username = :name")
    AppUser findByName(@Param("name") String userName);
}