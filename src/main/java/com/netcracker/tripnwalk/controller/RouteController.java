package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Route;
import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.RouteRepository;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

@RestController
public class RouteController {
    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Inject
    RouteRepository routeRepository;
    @Inject
    UserRepository userRepository;

    @RequestMapping(value = "/routes", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set> getRoutes() {
        Long id_user = 1L;
        try {
            User user = userRepository.findOne(id_user);
            return new ResponseEntity(user.getRoutes(), HttpStatus.OK);
        } catch (NullPointerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/routes", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> setRoute(@Valid @RequestBody Route route, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Long id_user = 1L;
        try {
            User user = userRepository.findOne(id_user);
            user.addRoute(routeRepository.save(route));
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NullPointerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Route> getRouteById(@PathVariable("id") Long id) {
        try {
            return new ResponseEntity<>(routeRepository.findOne(id), HttpStatus.OK);
        }catch (NullPointerException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<String> modifyById(@PathVariable("id") Long id, @Valid @RequestBody Route route, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (id == route.getId()) {
            routeRepository.save(route);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/routes/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteById(@PathVariable("id") Long id) {
        Long id_user = 1L;
        try {
            userRepository.findOne(id_user).getRoutes().remove(routeRepository.findOne(id));
            routeRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            logger.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}