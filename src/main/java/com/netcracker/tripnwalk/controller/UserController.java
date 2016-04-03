package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    @Inject
    UserRepository userRepository;

    @RequestMapping("/get-users")
    public ModelAndView getUsers() {
        ModelAndView model = new ModelAndView();
        List<User> usersList = new ArrayList<>();
        Iterable<User> all = userRepository.findAll();

        all.forEach(usersList::add);
        model.setViewName("users");
        model.addObject("usersList", usersList);

        return model;
    }

    @RequestMapping(value = "/add-user", method = RequestMethod.POST)
    public ResponseEntity<String> addUser(@RequestParam("userName") String userName) {
        userRepository.save(new User(userName));

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/set-friend", method = RequestMethod.POST)
    public String setFriend(@RequestParam("friend") String friendName, @RequestParam("userId") String userName) {
        User user = userRepository.findOne(Long.parseLong(userName));

        user.addFriend(userRepository.findByLogin(friendName));
        userRepository.save(user);

        return friendName;
    }
}
