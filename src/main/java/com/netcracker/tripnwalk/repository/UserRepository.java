package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("select u from User u where u.login = :login")
    User findByLogin(@Param("login") String userLogin);

    @Query("select u from User u where u.email = :email")
    User findByEmail(@Param("email") String userEmail);

    @Query("select u from User u where source_id = :source_id")
    User findByOauthID(@Param("source_id") String userOauthID);

    @Query("select u from User u where u.id != :id")
    Set<User> findAllOtherCurrent(@Param("id") Long id);

    @Query("select u from User u where u.id != :id and u.name = :name")
    Set<User> findByName(@Param("id") Long id, @Param("name") String name);

    @Query("select u from User u where u.id != :id and u.surname = :surname")
    Set<User> findBySurname(@Param("id") Long id, @Param("surname") String surname);

    @Query("select u from User u where u.id != :id and name = :name and surname = :surname")
    Set<User> findByFullName(@Param("id") Long id, @Param("name") String name, @Param("surname") String surname);
}