package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select u from Users u where u.name = :name")
    User findByName(@Param("name") String userName);
}