package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserStorage {
    User createUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUser(Long id);

    User addFriend(Long userId, Long friendId);

    User removeFriend(Long userId, Long friendId);

    Set<User> getFriends(Long userId);

    Set<User> getCommonFriends(Long userId, Long otherId);
}