package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.*;

@JdbcTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final JdbcTemplate jdbc;
    private final UserDbStorage userDbStorage;

    private final String firstName = "first name";
    private final String firstEmail = "first email";
    private final String firstLogin = "first login";
    private final String secondName = "second name";
    private final String secondEmail = "second email";
    private final String secondLogin = "second login";

    @BeforeEach
    void setUp() {
        jdbc.update("DELETE FROM likes");
        jdbc.update("DELETE FROM genres_in_film");
        jdbc.update("DELETE FROM films");
        jdbc.update("DELETE FROM friends");
        jdbc.update("DELETE FROM users");
        jdbc.update("ALTER SEQUENCE film_id_seq RESTART WITH 1");
        jdbc.update("ALTER SEQUENCE user_id_seq RESTART WITH 1");
    }

    private User createTestUser(String email, String login, String name) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(LocalDate.of(1990, 1, 1));
        return user;
    }

    @Test
    void testCreateUser() {
        User user = createTestUser(firstEmail, firstLogin, firstName);
        userDbStorage.createUser(user);
        User savedUser = userDbStorage.getUser(user.getId());
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isEqualTo(1L);
        assertThat(savedUser.getEmail()).isEqualTo(firstEmail);
        assertThat(savedUser.getLogin()).isEqualTo(firstLogin);
        assertThat(savedUser.getName()).isEqualTo(firstName);
        assertThat(savedUser.getBirthday()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void testGetUser() {
        User user = createTestUser(firstEmail, firstLogin, firstName);
        userDbStorage.createUser(user);
        Long userId = user.getId();
        User foundUser = userDbStorage.getUser(userId);
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(userId);
        assertThat(foundUser.getEmail()).isEqualTo(firstEmail);
        assertThat(foundUser.getFriends()).isEmpty();
    }

    @Test
    void testGetAllUsers() {
        User user1 = createTestUser(firstEmail, firstLogin, firstName);
        User user2 = createTestUser(secondEmail, secondLogin, secondName);
        userDbStorage.createUser(user1);
        userDbStorage.createUser(user2);
        List<User> users = userDbStorage.getUsers();
        List<Long> usersIds = users.stream().map(User::getId).toList();
        assertThat(usersIds).hasSize(2);
        assertThat(usersIds).contains(1L, 2L);
        assertThat(users.getFirst().getEmail()).isEqualTo(firstEmail);
        assertThat(users.getLast().getEmail()).isEqualTo(secondEmail);
    }

    @Test
    void testAddAndDeleteFriend() {
        User user1 = createTestUser(firstEmail, firstLogin, firstName);
        User user2 = createTestUser(secondEmail, secondLogin, secondName);
        userDbStorage.createUser(user1);
        userDbStorage.createUser(user2);
        Long user1Id = user1.getId();
        Long user2Id = user2.getId();
        userDbStorage.addFriend(user1Id, user2Id);
        User userWithFriend = userDbStorage.getUser(user1Id);
        List<Long> friendIds = userDbStorage.getFriends(userWithFriend.getId()).stream().map(User::getId).toList();
        assertThat(friendIds).contains(user2Id);
        userDbStorage.removeFriend(user1Id, user2Id);
        User userWithoutFriend = userDbStorage.getUser(user1Id);
        assertThat(userWithoutFriend.getFriends()).doesNotContain(user2Id);
    }

    @Test
    void testGetUserNotFound() {
        User foundUser = userDbStorage.getUser(45799889L);
        assertThat(foundUser).isNull();
    }

    @Test
    void testUpdateUser() {
        User user = createTestUser(firstEmail, firstLogin, firstName);
        userDbStorage.createUser(user);
        Long userId = user.getId();
        user.setName(firstName);
        user.setEmail(firstEmail);
        user.setLogin(firstLogin);
        userDbStorage.updateUser(user);
        User updatedUser = userDbStorage.getUser(userId);
        assertThat(updatedUser.getName()).isEqualTo(firstName);
        assertThat(updatedUser.getEmail()).isEqualTo(firstEmail);
        assertThat(updatedUser.getLogin()).isEqualTo(firstLogin);
    }
}
