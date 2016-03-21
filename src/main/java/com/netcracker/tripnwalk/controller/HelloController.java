package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Inject;


@Controller
public class HelloController {
    @Inject
    UserRepository userRepository;

    @RequestMapping("/")
    public String hello(Model model) {
        return "hello";
    }



}
