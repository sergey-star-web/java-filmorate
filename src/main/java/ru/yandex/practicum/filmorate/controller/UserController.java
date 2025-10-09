package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final InMemoryUserStorage userStorage;

    public UserController(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @GetMapping()
    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    @PutMapping
    public User update(@Valid @RequestBody User updateUser) {
        return userStorage.update(updateUser);
    }
}
