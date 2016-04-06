package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import javax.inject.Inject;

@RestController
public class LoginController extends HttpServlet {
    @Inject
    UserRepository userRepository;

    @Autowired
    private SessionController sessionController;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> getAuth(HttpServletRequest request, @RequestBody User user, Model model) {
        User userBD = userRepository.findByLogin(user.getLogin());
        if (userBD.getPassword().equals(user.getPassword())) {
            System.out.println(userBD.getId().toString());
            sessionController.setSessionId(userBD.getId().toString()); //создание сессии
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
