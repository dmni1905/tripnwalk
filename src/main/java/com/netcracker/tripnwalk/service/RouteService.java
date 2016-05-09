package com.netcracker.tripnwalk.service;

import com.netcracker.tripnwalk.entry.Route;
import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.RouteRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

@Service
public class RouteService {
    private static final Logger logger = LogManager.getLogger(RouteService.class);
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private UserService userService;

    public Optional<Set> getAllByUserId(Long idUser) {
        Optional<User> userCurrent = userService.getById(idUser);
        if (userCurrent.isPresent()) {
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
            return Optional.ofNullable(routeRepository.findOne(idRoute));
        } else {
            logger.error("GET ROUTES: User with id=" + idUser + " not found");

            return Optional.empty();
        }
    }

    public Optional<Route> modify(Long idUser, Route route) {
        Optional<User> userCurrent = userService.getById(idUser);
        Optional<Route> routeCurrent = getById(idUser, route.getId());

        if (userCurrent.isPresent() && routeCurrent.isPresent()) {
            return Optional.of(routeRepository.save(route));
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
