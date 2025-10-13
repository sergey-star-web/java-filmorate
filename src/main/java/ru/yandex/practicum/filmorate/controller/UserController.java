package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        if (userService.getUsers().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userService.getUsers());
        }
        return ResponseEntity.ok(userService.getUsers());
    }

    @PutMapping
    public ResponseEntity<User> update(@Valid @RequestBody User updateUser) {
        User user = userService.getUser(updateUser.getId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updateUser);
        }
        return ResponseEntity.ok(userService.updateUser(updateUser));
    }

    // Получение пользователя по идентификатору
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // Добавление пользователя в друзья
    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        return userService.addFriend(id, friendId);
    }

    // Удаление пользователя из друзей
    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public User removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
         return userService.removeFriend(id, friendId);
    }

    // Получение списка друзей пользователя
    @GetMapping("/{id}/friends")
    public ResponseEntity<Set<User>> getFriends(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getFriends(id));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<Set<User>> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        return ResponseEntity.ok(userService.getCommonFriends(id, otherId));
    }
}
