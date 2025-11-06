package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User updateUser(User updatedUser) {
        User user = userStorage.getUser(updatedUser.getId());
        if (user == null) {
            throw new NotFoundException();
        }
        return userStorage.updateUser(updatedUser);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public User addFriend(Long userId, Long friendId) {
         return userStorage.addFriend(userId, friendId);
    }

    public User removeFriend(Long userId, Long friendId) {
        return userStorage.removeFriend(userId, friendId);
    }

    public Set<User> getFriends(Long userId) {
        return userStorage.getFriends(userId);
    }

    public Set<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}