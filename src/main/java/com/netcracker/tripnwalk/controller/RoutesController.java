package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Routes;
import com.netcracker.tripnwalk.repository.RouteRepository;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

@RestController
public class RoutesController {
    @Inject
    RouteRepository itinerariesRepository;
    @Inject
    UserRepository userRepository;

    @RequestMapping(value = "/set-itineraries", method = RequestMethod.POST)
    public ResponseEntity<Void> setItineraries(@RequestParam("name") String name,
                                                 @RequestParam("duration") String duration,
                                                 @RequestParam("userId") String userId) {
        Routes itineraries = new Routes(name, java.sql.Time.valueOf(duration));

        userRepository.findOne(Long.parseLong(userId)).addItineraries(itineraries);
        itinerariesRepository.save(itineraries);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}