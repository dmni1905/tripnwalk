package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Route;
import com.netcracker.tripnwalk.entry.RoutePoint;
import com.netcracker.tripnwalk.repository.RoutePointRepository;
import com.netcracker.tripnwalk.repository.RouteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.List;

@RestController
public class PointController {
    @Inject
    RoutePointRepository pointRepository;
    @Inject
    RouteRepository routeRepository;

    @RequestMapping(value = "/points", method = RequestMethod.GET)
    public ModelAndView getPoints(@RequestParam("userRoutId") String userRoutId){
        Route one = routeRepository.findOne(Long.parseLong(userRoutId));

        return new ModelAndView("point", "point", one.getPoints());
    }

    @RequestMapping(value = "/add-point", method = RequestMethod.POST)
    public ResponseEntity<Void> addPoint(@RequestParam("userRoutId") String userRoutId,
                           @RequestParam("X") String x,
                           @RequestParam("Y") String y){
        RoutePoint point = new RoutePoint(Float.parseFloat(x), Float.parseFloat(y));

        routeRepository.findOne(Long.parseLong(userRoutId)).addPoint(point);
        pointRepository.save(point);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
