package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Itineraries;
import com.netcracker.tripnwalk.entry.ItinerariesPoint;
import com.netcracker.tripnwalk.repository.ItinerariesPointRepository;
import com.netcracker.tripnwalk.repository.ItinerariesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PointController {
    @Inject
    ItinerariesPointRepository pointRepository;
    @Inject
    ItinerariesRepository itinerariesRepository;


    @RequestMapping(value = "/points", method = RequestMethod.GET)
    public ModelAndView getPoints(@RequestParam("userRoutId") String userRoutId){
        Itineraries one = itinerariesRepository.findOne(Long.parseLong(userRoutId));

        return new ModelAndView("point", "point", one.getPoints());
    }

    @RequestMapping(value = "/addPoint", method = RequestMethod.POST)
    @ResponseBody
    public String addPoint(@RequestParam("userRoutId") String userRoutId,
                           @RequestParam("X") String x,
                           @RequestParam("Y") String y){
        ItinerariesPoint point = new ItinerariesPoint(Float.parseFloat(x), Float.parseFloat(y));

        itinerariesRepository.findOne(Long.parseLong(userRoutId)).addPoint(point);
        pointRepository.save(point);
        return "";
    }
}
