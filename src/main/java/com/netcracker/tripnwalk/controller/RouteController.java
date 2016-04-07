package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Route;
import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.RouteRepository;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Set;

@RestController
public class RouteController {
    @Inject
    RouteRepository routeRepository;
    @Inject
    UserRepository userRepository;

    @RequestMapping(value = "/routes", method = RequestMethod.GET, produces = "application/json")
    public Set getRoutes() {
        Long id_user = 1L;
        User user = userRepository.findOne(id_user);
        return user.getRoutes();
    }

    @RequestMapping(value = "/routes", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> setRoute(@RequestBody Route route) {
        Long id_user = 1L;
        routeRepository.save(route);
        User user = userRepository.findOne(id_user);
        user.addRoute(route);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.GET, produces = "application/json")
    public Route getRouteById(@PathVariable("id") Long id) {
        return routeRepository.findOne(id);
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<String> modifyById(@PathVariable("id") Long id, @RequestBody Route route) {
        if (id == route.getId()) {
            routeRepository.save(route);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        routeRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}