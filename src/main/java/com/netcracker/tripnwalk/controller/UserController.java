package com.netcracker.tripnwalk.controller;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.*;

@RestController
public class UserController {
    @Inject
    UserRepository userRepository;

    @Autowired
    private SessionController sessionController;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getUser() {
        Long id = 1L;
        return userRepository.findOne(id).toString();
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<String> modifyUser(@RequestBody User user) {
        User userDb = userRepository.findOne(user.getId());

        Arrays.stream(User.class.getDeclaredFields()).forEach(f -> {
            mergeUserByField(userDb, user, f.getName());
        });
        userRepository.save(userDb);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUser() {
        Long id = 1L;
        userRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET, produces = "application/json")
    public Set<String> getFriends() {
        Long id = 1L;
        Set<String> friend = new HashSet();
        userRepository.findOne(id).getFriends().stream().forEach(f -> {
            friend.add(f.toString());
        });
        return friend;
    }

    @RequestMapping(value = "/friends", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> modifyFriend(@RequestBody User user){
        userRepository.save(user);
        Long id = 1L;
        User userDB = userRepository.findOne(id);
        userDB.addFriend(user);
        userRepository.save(userDB);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/friends/{id}", method = RequestMethod.DELETE, produces = "application/json")
    public ResponseEntity<String> deleteFriend(@PathVariable("id") Long id){
        Long id_user = 1L;
        User user = userRepository.findOne(id_user);
        user.getFriends().remove(userRepository.findOne(id));
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void mergeUserByField(User userDb, User userReq, String field) {
        switch (field) {
            case "name":
                if (!userDb.getName().equals(userReq.getName())) {
                    userDb.setName(userReq.getName());
                }
                break;
            case "surname":
                if (!userDb.getSurname().equals(userReq.getSurname())) {
                    userDb.setSurname(userReq.getSurname());
                }
                break;
            case "login":
                if (!userDb.getLogin().equals(userReq.getLogin())) {
                    userDb.setLogin(userReq.getLogin());
                }
                break;
            case "birthDate":
                if (!userDb.getBirthDate().equals(userReq.getBirthDate())) {
                    userDb.setBirthDate(userReq.getBirthDate());
                }
                break;
            case "email":
                if (!userDb.getEmail().equals(userReq.getEmail())) {
                    userDb.setEmail(userReq.getEmail());
                }
                break;
        }
    }
}
