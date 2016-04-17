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
    public ResponseEntity<User> getUser() {
        Long id = 1L;

        Optional<User> user = userService.getById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public ResponseEntity<String> setUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userService.save(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<String> modifyUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userService.modify(user)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser() {
        Long id = 1L;
        if (userService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set> getFriends() {
        Long id = 1L;
        Optional<User> user = userService.getById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get().getFriends(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/friends/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> modifyFriend(@PathVariable("id") Long idFriend) {
        Long idBd = 1L;
        Optional<User> user = userService.getById(idBd);
        if (userService.addFriend(user, idFriend)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value = "/friends/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> deleteFriend(@PathVariable("id") Long idFriend) {
        Long idBd = 1L;
        Optional<User> user = userService.getById(idBd);
        if (userService.deleteFriend(user, idFriend)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }


}
