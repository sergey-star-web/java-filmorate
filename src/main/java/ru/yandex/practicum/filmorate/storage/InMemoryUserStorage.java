package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FriendsAddException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    @Override
    public User createUser(User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        user.setId(genNextId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан. Созданный пользователь: {}", user);
        return user;
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найдено");
        }
        if (friend == null) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найдено");
        }
        if (user.getFriends().contains(friendId) || friend.getFriends().contains(userId)) {
            throw new FriendsAddException("Пользователи с id " + userId + " и " + friendId +
                    " уже в друзьях друг у друга");
        }
        user.addFriend(friendId);
        friend.addFriend(userId);
        log.info("Пользователь {} добавил в друзья пользователя {} ", userId, friendId);
        return user;
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найдено");
        }
        if (friend == null) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найдено");
        }
        if (!user.getFriends().contains(friendId) || !friend.getFriends().contains(userId)) {
            log.info("Попытка удалить несуществующую дружбу между {} и {}", userId, friendId);
            return user;
        }
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        log.info("Пользователь {} удалил из друзей пользователя {}", userId, friendId);
        return user;
    }

    @Override
    public Set<User> getFriends(Long id) {
        User user = users.get(id);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        return user.getFriends().stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriends(Long userId, Long otherId) {
        User user = users.get(userId);
        User otherUser = users.get(otherId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (otherUser == null) {
            throw new NotFoundException("Пользователь с id " + otherId + " не найден");
        }
        Set<Long> commonFriendIds = new HashSet<>(user.getFriends());
        commonFriendIds.retainAll(otherUser.getFriends());
        return commonFriendIds.stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }

    private Long genNextId() {
        return idCounter++;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long id) {
        return users.get(id);
    }

    @Override
    public User updateUser(User updateUser) {
        Long id = updateUser.getId();
        throwIfNoUser(id);
        users.put(updateUser.getId(), updateUser);
        log.info("Пользователь успешно обновлен. Измененный пользователь: {}", updateUser);
        return updateUser;
    }

    private void throwIfNoUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            String errorMessage = String.format("Не найден пользователь с %d", id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }
}
