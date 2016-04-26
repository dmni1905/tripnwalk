package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Route;
import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.RouteRepository;
import com.netcracker.tripnwalk.repository.UserRepository;
import com.netcracker.tripnwalk.service.RouteService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@RestController
public class RouteController {
    private static final Logger logger = LogManager.getLogger(RouteController.class);

    @Autowired
    RouteService routeService;

    @Inject
    RouteRepository routeRepository;
    @Inject
    UserRepository userRepository;

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

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Route> getRouteById(@PathVariable("id") Long idRoute) {
        Long idUser = sessionBean.getSessionId();
        Optional<Route> routeFromDB = routeService.getById(idUser, idRoute);
        if (routeFromDB.isPresent()) {
            return new ResponseEntity<>(routeFromDB.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<Route> modifyById(@PathVariable("id") Long id, @Valid @RequestBody Route route, BindingResult bindingResult) {
        Long idUser = sessionBean.getSessionId();
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id == route.getId()) {
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
    public ResponseEntity<String> deleteById(@PathVariable("id") Long idRoute) {
        Long idUser = sessionBean.getSessionId();
        if(routeService.delete(idUser, idRoute)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}