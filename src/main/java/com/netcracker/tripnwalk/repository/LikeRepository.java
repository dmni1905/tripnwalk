package com.netcracker.tripnwalk.repository;

import com.netcracker.tripnwalk.entry.Like;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface LikeRepository extends CrudRepository<Like, Long> {
    @Query("select u from Like u where u.key.idUser = :user_id and u.key.idRoute = :route_id")
    Like findLike(@Param("user_id") Long userId, @Param("route_id") Long routeId);

    @Query("select count(u) from Like u where u.key.idRoute = :route_id")
    Integer getCountLikes(@Param("route_id") Long routeId);

    @Query("select key.idRoute, count(*) from Like group by key.idRoute order by count(*) desc")
    List<Object> getTopList(Pageable pageable);
}
