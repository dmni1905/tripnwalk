package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.Users;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @Inject
    UserRepository userRepository;

    @RequestMapping("/getUsers")
    public ModelAndView getUsers() {
        ModelAndView model = new ModelAndView();
        model.setViewName("users");
        List<Users> userstList = new ArrayList<>();
        Iterable<Users> all = userRepository.findAll();
        all.forEach(u -> {
            userstList.add(u);
        });
        model.addObject("usersList", userstList);
        return model;
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addUser(@RequestParam("userName") String userName) {
        userRepository.save(new Users(userName));
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value = "/setFriend", method = RequestMethod.POST)
    @ResponseBody
    public String setFriend(@RequestParam("friend") String friendName, @RequestParam("userId") String userName) {
        Users user = userRepository.findOne(Long.parseLong(userName));
        user.addFriend(userRepository.findByName(friendName));
        userRepository.save(user);
        return friendName;
    }

}
