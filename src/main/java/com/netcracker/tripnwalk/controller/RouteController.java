package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Route;
import com.netcracker.tripnwalk.repository.RouteRepository;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class RouteController {
    @Inject
    RouteRepository routeRepository;
    @Inject
    UserRepository userRepository;

    @RequestMapping(value = "/set-route", method = RequestMethod.POST)
    public ResponseEntity<Void> setRoute(@RequestParam("name") String name,
                                                 @RequestParam("duration") String duration,
                                                 @RequestParam("userId") String userId) {
        Route route = new Route(name, java.sql.Time.valueOf(duration));

        userRepository.findOne(Long.parseLong(userId)).addRoute(route);
        routeRepository.save(route);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}