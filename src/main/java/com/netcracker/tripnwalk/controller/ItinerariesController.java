package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Itineraries;
import com.netcracker.tripnwalk.repository.ItinerariesRepository;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
public class ItinerariesController {
    @Inject
    ItinerariesRepository itinerariesRepository;
    @Inject
    UserRepository userRepository;

    //  сохранение маршрута в базу
    @RequestMapping(value = "/setItineraries", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> setItineraries(@RequestParam("name") String name,
                                                 @RequestParam("duration") String duration,
                                                 @RequestParam("userId") String userId) {
        Itineraries itineraries = new Itineraries(name, java.sql.Time.valueOf(duration));
        userRepository.findOne(Long.parseLong(userId)).addItineraries(itineraries);
        itinerariesRepository.save(itineraries);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
