package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import com.netcracker.tripnwalk.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    private SessionBean sessionBean;

    @Inject
    UserRepository userRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView index() {
        Optional<Long> sessionId = Optional.ofNullable(sessionBean.getSessionId());
        if (sessionId.isPresent()) {
            return getUser(sessionId.get());
        } else {
            return new ModelAndView("index");
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ModelAndView getUser(@PathVariable("id") Long id) {
        if (Optional.ofNullable(sessionBean.getSessionId()).isPresent()) {
            Optional<User> user = userService.getById(id);
            user.get().getFriends().forEach(f -> {
                f.getFriends().clear();
                f.getRoutes().clear();
            });
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.setViewName("main-page");
            modelAndView.addObject("user", user.get());
            if (id.equals(sessionBean.getSessionId())) {
                modelAndView.addObject("isMy", true);
            } else {
                modelAndView.addObject("isMy", false);
            }
            return modelAndView;
        } else {
            return new ModelAndView("index");
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<String> setUser(@RequestBody User user) {
        Optional<User> userBDLogin = userService.getByLogin(user.getLogin());
        Optional<User> userBDEmail = userService.getByEmail(user.getEmail());
        if (userBDLogin.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (userBDEmail.isPresent()) {
            User userFromBD = userBDEmail.get();
            userFromBD.setLogin(user.getLogin());
            userFromBD.setPassword(user.getPassword());
            if ((user.getBirthDate() != null) && (userFromBD.getBirthDate() == null)) {
                userFromBD.setBirthDate(user.getBirthDate());
            }
            userRepository.save(userFromBD);
            sessionBean.setSessionId(userFromBD.getId());
        } else {
            user.setImgSrc("http://www.nbb.go.th/images/blank_person[1].jpg");
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
            userBDLogin = userService.save(user);
            User userFromBD = userBDLogin.get();
            sessionBean.setSessionId(userFromBD.getId());
        }
        String session = sessionBean.getSessionId().toString();
        return new ResponseEntity<>(session, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<User> modifyUser(@PathVariable("id") Long id, @Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (Optional.ofNullable(sessionBean.getSessionId()).isPresent() && id.equals(sessionBean.getSessionId())) {
            Optional<User> userFromDB = userService.modify(user);
            if (userFromDB.isPresent()) {
                return new ResponseEntity<>(userFromDB.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/find-user", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set> findUser(@RequestParam("name") String name, @RequestParam("surname") String surname) {
        Long id = sessionBean.getSessionId();
        return new ResponseEntity<>(userService.findUsers(id, name, surname), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/friends", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Set> getFriends(@PathVariable("id") Long id) {
        if (Optional.ofNullable(sessionBean.getSessionId()).isPresent() && id.equals(sessionBean.getSessionId())) {
            Optional<Set<User>> friends = userService.getFriends(id);
            if (friends.isPresent()) {
                return new ResponseEntity<>(friends.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/{id}/friends/{id_friend}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<User> modifyFriend(@PathVariable("id") Long id, @PathVariable("id_friend") Long idFriend) {
        Long idBd = sessionBean.getSessionId();
        if (Optional.ofNullable(idBd).isPresent() && id.equals(idBd)) {
            Optional<User> user = userService.getById(idBd);
            if (userService.addFriend(user, idFriend)) {
                return new ResponseEntity<>(userService.getById(idFriend).get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/{id}/friends/{id_friend}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> deleteFriend(@PathVariable("id") Long id, @PathVariable("id_friend") Long idFriend) {
        Long idBd = sessionBean.getSessionId();
        if (Optional.ofNullable(idBd).isPresent() && id.equals(idBd)) {
            Optional<User> user = userService.getById(idBd);
            if (userService.deleteFriend(user, idFriend)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


}
