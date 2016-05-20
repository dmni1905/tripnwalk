package com.netcracker.tripnwalk.service;

import com.netcracker.tripnwalk.entry.Like;
import com.netcracker.tripnwalk.entry.components.TopLike;
import com.netcracker.tripnwalk.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    UserService userService;
    @Autowired
    RouteService routeService;

    public List<TopLike> getTop() {
        List<TopLike> top = new ArrayList<>();
        List<Object> topList = likeRepository.getTopList(new PageRequest(0, 5));
        for (Object cdata : topList) {
            Object[] obj = (Object[]) cdata;
            Long id = (Long) obj[0];
            Long count = (Long) obj[1];
            top.add( new TopLike(count, id));
        }
        return top;
    }

    public boolean like(Long userId, Long routeId) {
        Optional<Like> like = Optional.ofNullable(likeRepository.findLike(userId, routeId));
        if (like.isPresent()) {
            likeRepository.delete(like.get());
        } else {
            if (check(userId, routeId)) {
                Like newLike = new Like(userId, routeId);
                likeRepository.save(newLike);
                return true;
            }
        }
        return false;
    }

    public boolean getLikeStatus(Long userId, Long routeId) {
        return Optional.ofNullable(likeRepository.findLike(userId, routeId)).isPresent();
    }

    public Integer getListLikes(Long routeId) {
        return likeRepository.getCountLikes(routeId);
    }

    private boolean check(Long userId, Long routeId) {
        return userService.getById(userId).isPresent() && routeService.getById(routeId).isPresent();
    }
}
