package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.AppUser;
import com.netcracker.tripnwalk.entry.Itineraries;
import com.netcracker.tripnwalk.repository.ItinerariesRepository;
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

    //  получение списка пользователей (вместе со списком маршрутов и друзей)
    @RequestMapping("/getUsers")
    public ModelAndView getUsers() {
        ModelAndView model = new ModelAndView();
        model.setViewName("users");
        List<AppUser> userstList = new ArrayList<>();
        Iterable<AppUser> all = userRepository.findAll();
        all.forEach(u -> {
            userstList.add(u);
        });
        model.addObject("usersList", userstList);
        return model;
    }

    //  добавление нового пользователя
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addUser(@RequestParam("userName") String userName) {
        userRepository.save(new AppUser(userName));
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    //  добавление к пользователю userName друга friendName
    @RequestMapping(value = "/setFriend", method = RequestMethod.POST)
    @ResponseBody
    public String setFriend(@RequestParam("friend") String friendName, @RequestParam("userId") String userName) {
        AppUser user = userRepository.findOne(Long.parseLong(userName));
        user.addFriend(userRepository.findByName(friendName));
        userRepository.save(user);
        return friendName;
    }

}
