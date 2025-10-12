package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private Map<Long, Set<Long>> friends = new HashMap<>();
    private Long idCounter = 1L;

    public User create(User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        user.setId(genNextId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан. Созданный пользователь: {}", user);
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        if (users.containsKey(userId) && users.containsKey(friendId)) {
            friends.computeIfAbsent(userId, k -> new HashSet<>()).add(friendId);
            friends.computeIfAbsent(friendId, k -> new HashSet<>()).add(userId); // двусторонняя связь
        } else {
            // Обработка случая, когда пользователь не найден
            throw new IllegalArgumentException("User not found");
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        friends.getOrDefault(userId, new HashSet<>()).remove(friendId);
        friends.getOrDefault(friendId, new HashSet<>()).remove(userId); // удаление двусторонней связи
    }

    public List<User> getFriends(Long userId) {
        return friends.getOrDefault(userId, new HashSet<>())
                .stream()
                .map(users::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> friendsOfFirstUser = friends.getOrDefault(userId, new HashSet<>());
        Set<Long> friendsOfSecondUser = friends.getOrDefault(otherId, new HashSet<>());

        return friendsOfFirstUser.stream()
                .filter(friendsOfSecondUser::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }

    private Long genNextId() {
        return idCounter++;
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUser(Long id) {
        return users.get(id);
    }

    public User update(User updateUser) {
        Long id = updateUser.getId();
        isNullUser(id);
        users.put(updateUser.getId(), updateUser);
        log.warn("Пользователь успешно обновлен. Измененный пользователь: {}", updateUser);
        return updateUser;
    }

    private void isNullUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            String errorMessage = String.format("Не найден пользователь с %d", id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }
}
