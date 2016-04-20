package com.netcracker.tripnwalk.service;

import com.netcracker.tripnwalk.entry.User;
import com.netcracker.tripnwalk.repository.UserRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    UserRepository userRepository;

    public Optional<User> getById(Long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    public boolean save(User user) {
        return Optional.of(userRepository.save(user)).isPresent();
    }

    public boolean modify(User user) {
        Optional<User> userCurrent = getById(user.getId());
        if (userCurrent.isPresent()) {
            return Optional.of(userRepository.save(user)).isPresent();
        } else {
            logger.error("MODIFY: User with id=" + user.getId() + " not found");
            return false;
        }
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
            if(userCurrent.isPresent()){
                logger.error("DELETE FRIEND: User with id=" + userCurrent.get().getId() + " not found");
            } else {
                logger.error("DELETE FRIEND: User with id=" + friendId + " not found");
            }
            return false;
        }
    }
}
