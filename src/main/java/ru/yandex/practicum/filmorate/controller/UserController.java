package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private HashMap<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        user.setId(genNextId());
        users.put(user.getId(), user);
        log.info("Пользователь успешно создан. Созданный пользователь: {}", user);
        return user;
    }

    private Long genNextId() {
        return idCounter++;
    }

    @GetMapping()
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
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
