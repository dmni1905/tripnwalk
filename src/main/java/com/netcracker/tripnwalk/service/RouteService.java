package com.netcracker.tripnwalk.service;

import com.netcracker.tripnwalk.entry.Route;
import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.RouteRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RouteService {
    private static final Logger logger = LogManager.getLogger(RouteService.class);
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private LikeService likeService;
    @Autowired
    private UserService userService;

    public Optional<Route> getById(Long id) {
        return Optional.ofNullable(routeRepository.findOne(id));
    }

    public Route getRandom(List<Long> routeId) {
        return routeRepository.getTopList(routeId, new PageRequest(0,1)).get(0);
    }

    public Long getCount() {
        return routeRepository.getCointRow();
    }

    public Optional<Set> getAllByUserId(Long idUser) {
        Optional<User> userCurrent = userService.getById(idUser);
        if (userCurrent.isPresent()) {
            userCurrent.get().getRoutes().forEach(r -> {
                r.setLikes(likeService.getListLikes(r.getId()));
                r.setLikeForCurrentUser(likeService.getLikeStatus(idUser, r.getId()));
            });
            return Optional.ofNullable(userCurrent.get().getRoutes());
        } else {
            logger.error("GET ROUTES: User with id=" + idUser + " not found");

            return Optional.empty();
        }
    }

    public Optional<Route> add(Long idUser, Route route) {
        Optional<User> userCurrent = userService.getById(idUser);
        if (userCurrent.isPresent()) {
            userCurrent.get().addRoute(route);
            User userFromDB = userService.save(userCurrent.get()).get();

            return userFromDB.getRoutes().stream().filter(r -> r.getName().equals(route.getName())).findFirst();
        } else {
            logger.error("SAVE ROUTES: User with id=" + idUser + " not found");

            return Optional.empty();
        }
    }

    public Optional<Route> getById(Long idUser, Long idRoute) {
        Optional<User> userCurrent = userService.getById(idUser);

        if (userCurrent.isPresent()) {
            Optional<Route> rote = Optional.ofNullable(routeRepository.findOne(idRoute));
            if (rote.isPresent()) {
                rote.get().setLikes(likeService.getListLikes(rote.get().getId()));
                rote.get().setLikeForCurrentUser(likeService.getLikeStatus(idUser, idRoute));

            }
            return rote;
        } else {
            logger.error("GET ROUTES: Route with id=" + idRoute + " not found");

            return Optional.empty();
        }
    }

    public Optional<Route> modify(Long idUser, Route route) {
        Optional<User> userCurrent = userService.getById(idUser);
        Optional<Route> routeCurrent = getById(idUser, route.getId());

        if (userCurrent.isPresent() && routeCurrent.isPresent()) {
            Optional<Route> rote = Optional.of(routeRepository.save(route));
            if (rote.isPresent()) {
                rote.get().setLikes(likeService.getListLikes(rote.get().getId()));
                rote.get().setLikeForCurrentUser(likeService.getLikeStatus(idUser, route.getId()));
            }
            return rote;
        } else {
            if (!userCurrent.isPresent()) {
                logger.error("MODIFY ROUTES: User with id=" + idUser + " not found");
            } else {
                logger.error("MODIFY ROUTES: Route with id=" + route.getId() + " not found");
            }

            return Optional.empty();
        }
    }

    public boolean delete(Long idUser, Long idRoute) {
        Optional<User> userCurrent = userService.getById(idUser);
        Optional<Route> routeCurrent = getById(idUser, idRoute);

        if (userCurrent.isPresent() && routeCurrent.isPresent()) {
            getAllByUserId(idUser).get().remove(routeCurrent.get());
            routeRepository.delete(idRoute);

            return true;
        } else {
            if (!userCurrent.isPresent()) {
                logger.error("DELETE ROUTES: User with id=" + idUser + " not found");
            } else {
                logger.error("DELETE ROUTES: Route with id=" + idRoute + " not found");
            }

            return false;
        }
    }


}
