package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User updateUser(User updatedUser) {
        return userStorage.update(updatedUser);
    }

    public User getUser(Long id) {
        return userStorage.getUser(id);
    }

    public void addFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        if (user != null && friend != null) {
            user.addFriend(friendId);
            userStorage.update(user);
        } else {
            throw new IllegalArgumentException("Пользователь или друг не найдены");
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userStorage.getUser(userId);
        if (user != null) {
            user.removeFriend(friendId);
            userStorage.update(user);
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
    }

    public List<User> getFriends(Long userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        Set<Long> friendIds = user.getFriends();
        List<User> friends = new ArrayList<>();
        for (Long friendId : friendIds) {
            User friend = userStorage.getUser(friendId);
            if (friend != null) {
                friends.add(friend);
            }
        }
        return friends;
    }
}