package com.netcracker.tripnwalk.service;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.Tree;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    public Set<User> findUsers(Long curId, String name, String surname) {
        if (name.isEmpty() && surname.isEmpty()) {
            return userRepository.findAllOtherCurrent(curId);
        } else if (!name.isEmpty() && !surname.isEmpty()) {
            return userRepository.findByFullName(curId, name, surname);
        } else if (name.isEmpty()) {
            return userRepository.findBySurname(curId, surname);
        } else {
            return userRepository.findByName(curId, name);
        }
    }

    public Optional<User> getById(Long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public Optional<User> getByLogin(String login) {
        return Optional.ofNullable(userRepository.findByLogin(login));
    }

    public Optional<User> save(User user) {
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> modify(User user) {
        User userDB = getById(user.getId()).get();
        Arrays.stream(User.class.getDeclaredFields()).forEach(f -> mergeUserByField(userDB, user, f.getName()));
        return Optional.of(userRepository.save(userDB));
    }

    public boolean delete(Long id) {
        Optional<User> userCurrent = getById(id);
        if (userCurrent.isPresent()) {
            User user = userCurrent.get();
            user.getFriends().forEach(u -> {
                u.getFriends().remove(user);
                userRepository.save(u);
            });
            user.getFriends().clear();
            userRepository.delete(id);
            return true;
        } else {
            logger.error("DELETE: User with id=" + id + " not found");
            return false;
        }
    }

    public Optional<Set<User>> getFriends(Long id){
        Optional<User> userCurrent = getById(id);
        if (userCurrent.isPresent()) {
            return Optional.of((userCurrent.get().getFriends()));
        } else{
            return Optional.empty();
        }
    }

    public boolean addFriend(Optional<User> userCurrent, Long friendId) {
        Optional<User> userFriend = getById(friendId);
        if (userFriend.isPresent() && userCurrent.isPresent()) {
            userCurrent.get().addFriend(userFriend.get());
            return Optional.of(userRepository.save(userCurrent.get())).isPresent();
        } else {
            if (userCurrent.isPresent()) {
                logger.error("ADD FRIEND: User with id=" + userCurrent.get().getId() + " not found");
            } else {
                logger.error("ADD FRIEND: User with id=" + friendId + " not found");
            }
            return false;
        }
    }

    public boolean deleteFriend(Optional<User> userCurrent, Long friendId) {
        Optional<User> userFriend = getById(friendId);
        if (userFriend.isPresent() && userCurrent.isPresent()) {
            userCurrent.get().getFriends().remove(userFriend.get());
            userFriend.get().getFriends().remove(userCurrent.get());
            return
                    Optional.of(userRepository.save(userFriend.get())).isPresent() &&
                            Optional.of(userRepository.save(userCurrent.get())).isPresent();
        } else {
            if (userCurrent.isPresent()) {
                logger.error("DELETE FRIEND: User with id=" + userCurrent.get().getId() + " not found");
            } else {
                logger.error("DELETE FRIEND: User with id=" + friendId + " not found");
            }
            return false;
        }
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
//            case "login":
//                if (!userDb.getLogin().equals(userReq.getLogin())) {
//                    userDb.setLogin(userReq.getLogin());
//                }
//                break;
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
