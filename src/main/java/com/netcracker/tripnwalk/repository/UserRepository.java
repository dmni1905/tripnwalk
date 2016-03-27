package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Long> {
    @Query("select u from Users u where u.name = :name")
    Users findByName(@Param("name") String userName);
}