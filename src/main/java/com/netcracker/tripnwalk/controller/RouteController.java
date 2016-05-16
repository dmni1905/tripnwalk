package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Route;
import com.netcracker.tripnwalk.entry.RoutePoint;
import com.netcracker.tripnwalk.service.RouteService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
public class RouteController {
    private static final Logger logger = LogManager.getLogger(RouteController.class);

    @Autowired
    private RouteService routeService;
    @Autowired
    private SessionBean sessionBean;

    @RequestMapping(value = "/routes", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set> getRoutes() {
        Long idUser = sessionBean.getSessionId();
        Optional<Set> routes = routeService.getAllByUserId(idUser);
        if (routes.isPresent()) {
            return new ResponseEntity(routes.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/routes", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Route> addRoute(@Valid @RequestBody Route route, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long idUser = sessionBean.getSessionId();
        Optional<Route> routeFromDB = routeService.add(idUser, route);
        if (routeFromDB.isPresent()) {
            return new ResponseEntity<>(routeFromDB.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> copyRoute(@PathVariable("id") Long idRoute, @RequestBody Long friend_id) throws CloneNotSupportedException {
        Long idUser = sessionBean.getSessionId();
        Optional<Route> friendRoute = routeService.getById(friend_id, idRoute);
        if (friendRoute.isPresent()) {
            Route myRoute = new Route(friendRoute.get().getName());
            for (RoutePoint point : friendRoute.get().getPoints()) {
                myRoute.addPoint(point.clone());
            }
            for (RoutePoint point : myRoute.getPoints()) {
                point.setId(null);
            }
            routeService.add(idUser, myRoute);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Route> getRoute(@PathVariable("id") Long idRoute) {
        Long idUser = sessionBean.getSessionId();
        Optional<Route> routeFromDB = routeService.getById(idUser, idRoute);
        if (routeFromDB.isPresent()) {
            return new ResponseEntity<>(routeFromDB.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<Route> updateRoute(@PathVariable("id") Long id,
                                             @Valid @RequestBody Route route,
                                             BindingResult bindingResult) {
        Long idUser = sessionBean.getSessionId();
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id.equals(route.getId())) {
            Optional<Route> routeFromDB = routeService.modify(idUser, route);
            if (routeFromDB.isPresent()) {
                return new ResponseEntity<>(routeFromDB.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteRoute(@PathVariable("id") Long idRoute) {
        Long idUser = sessionBean.getSessionId();
        if (routeService.delete(idUser, idRoute)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}