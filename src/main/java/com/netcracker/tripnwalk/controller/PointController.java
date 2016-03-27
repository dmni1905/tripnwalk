package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Routes;
import com.netcracker.tripnwalk.entry.RoutePoint;
import com.netcracker.tripnwalk.repository.RoutePointRepository;
import com.netcracker.tripnwalk.repository.RouteRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

@Controller
public class PointController {
    @Inject
    RoutePointRepository pointRepository;
    @Inject
    RouteRepository itinerariesRepository;


    @RequestMapping(value = "/points", method = RequestMethod.GET)
    public ModelAndView getPoints(@RequestParam("userRoutId") String userRoutId){
        Routes one = itinerariesRepository.findOne(Long.parseLong(userRoutId));

        return new ModelAndView("point", "point", one.getPoints());
    }

    @RequestMapping(value = "/addPoint", method = RequestMethod.POST)
    @ResponseBody
    public String addPoint(@RequestParam("userRoutId") String userRoutId,
                           @RequestParam("X") String x,
                           @RequestParam("Y") String y){
        RoutePoint point = new RoutePoint(Float.parseFloat(x), Float.parseFloat(y));

        itinerariesRepository.findOne(Long.parseLong(userRoutId)).addPoint(point);
        pointRepository.save(point);
        return "";
    }
}
