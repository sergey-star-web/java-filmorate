package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private HashMap<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    public User create(User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        user.setId(genNextId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан. Созданный пользователь: {}", user);
        return user;
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
        User user = users.get(id);
        isNullUser(user);
        users.put(updateUser.getId(), updateUser);
        log.info("Пользователь успешно обновлен. Измененный пользователь: {}", updateUser);
        return updateUser;
    }

    private void isNullUser(User user) {
        if (user == null) {
            String errorMessage = String.format("Не найден пользователь с %d", user.getId());
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }
}
