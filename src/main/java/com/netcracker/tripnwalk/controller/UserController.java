package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import com.netcracker.tripnwalk.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private SessionBean sessionBean;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getUser() {
//        if (Optional.ofNullable(sessionBean.getSessionId()).isPresent()) {
//            Long id = sessionBean.getSessionId();
//            Optional<User> user = userService.getById(id);
//            return new ModelAndView("index", "user", user.get());
//            if (user.isPresent()) {
//                return new ResponseEntity<>(user.get(), HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//            }
//        } else {
            return new ModelAndView("index");
//        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<User> setUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> userFromDB = userService.save(user);
        if (userFromDB.isPresent()) {
            return new ResponseEntity<>(userFromDB.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<User> modifyUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> userFromDB = userService.modify(user);
        if (userFromDB.isPresent()) {
            return new ResponseEntity<>(userFromDB.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/find-user", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set> findUser(@RequestParam("name") String name, @RequestParam("surname") String surname) {
        Long id = sessionBean.getSessionId();
        return new ResponseEntity<>(userService.findUsers(id, name, surname), HttpStatus.OK);
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set> getFriends() {
        Long id = sessionBean.getSessionId();
        Optional<Set<User>> friends = userService.getFriends(id);
        if (friends.isPresent()) {
            return new ResponseEntity<>(friends.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/friends/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<User> modifyFriend(@PathVariable("id") Long idFriend) {
        Long idBd = sessionBean.getSessionId();
        Optional<User> user = userService.getById(idBd);
        if (userService.addFriend(user, idFriend)) {
            return new ResponseEntity<>(userService.getById(idFriend).get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/friends/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> deleteFriend(@PathVariable("id") Long idFriend) {
        Long idBd = sessionBean.getSessionId();
        Optional<User> user = userService.getById(idBd);
        if (userService.deleteFriend(user, idFriend)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
