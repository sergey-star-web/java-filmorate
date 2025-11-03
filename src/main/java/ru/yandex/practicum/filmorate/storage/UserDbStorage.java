package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.FriendsAddException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private final String findAllQuery = "SELECT * FROM users";
    private final String findByIdQuery = "SELECT * FROM users WHERE id = ?";
    private final String insertUsersQuery = "INSERT INTO users(id, email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?, ?)";
    private final String updateQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private final String insertFriendsQuery = "INSERT INTO friends(user_send_id, user_received_id, " +
            "confirm_friendship_status) values (?, ?, true)";
    private final String findByAllFriendsQuery = "select u.* from users as u inner join friends as f " +
            "on u.id = f.user_received_id where f.user_send_id = ?";
    private final String deleteFriendsQuery = "DELETE FROM friends WHERE user_send_id = ? AND user_received_id = ?";
    @Autowired
    private JdbcTemplate jdbc;

    public UserDbStorage(JdbcTemplate jdbc, UserRowMapper mapper) {
        super(jdbc, mapper, User.class);
    }

    @Override
    public User createUser(User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        Long id = jdbc.queryForObject("SELECT NEXT VALUE FOR user_id_seq", Long.class);
        user.setId(id);
        insert(insertUsersQuery,
                id,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        log.info("Пользователь успешно создан. Созданный пользователь: {}", user);
        return getUser(id);
    }

    @Override
    public User addFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найдено");
        }
        if (friend == null) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найдено");
        }
        if (user.getFriends().containsKey(friendId) || friend.getFriends().containsKey(userId)) {
            throw new FriendsAddException("Пользователи с id " + userId + " и " + friendId +
                    " уже в друзьях друг у друга");
        }
        insert(insertFriendsQuery,
                user.getId(),
                friend.getId()
        );
        log.info("Пользователь {} добавил в друзья пользователя {} ", userId, friendId);
        return user;
    }

    @Override
    public User removeFriend(Long userId, Long friendId) {
        User user = getUser(userId);
        User friend = getUser(friendId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найдено");
        }
        if (friend == null) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найдено");
        }
        if (!getFriends(userId).contains(friend)) {
            log.info("Попытка удалить несуществующую дружбу между {} и {}", userId, friendId);
            return user;
        }
        update(deleteFriendsQuery,
                userId,
                friendId
        );
        log.info("Пользователь {} удалил из друзей пользователя {}", userId, friendId);
        return user;
    }

    @Override
    public Set<User> getFriends(Long id) {
        User user = getUser(id);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден");
        }
        List<User> friends = jdbc.query(findByAllFriendsQuery, new UserRowMapper(), id);
        return new HashSet<>(friends);
    }

    @Override
    public Set<User> getCommonFriends(Long userId, Long otherId) {
        User user = getUser(userId);
        User otherUser = getUser(otherId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (otherUser == null) {
            throw new NotFoundException("Пользователь с id " + otherId + " не найден");
        }
        Set<User> friendUser = getFriends(userId);
        Set<User> friendOtherUser = getFriends(otherId);
        friendUser.retainAll(friendOtherUser);
        return friendUser;
    }

    @Override
    public List<User> getUsers() {
        return findMany(findAllQuery);
    }

    @Override
    public User getUser(Long id) {
        Optional<User> filmOptional = findOne(findByIdQuery, id);
        return filmOptional.orElse(null);
    }

    @Override
    public User updateUser(User updateUser) {
        Long id = updateUser.getId();
        update(
                updateQuery,
                updateUser.getEmail(),
                updateUser.getLogin(),
                updateUser.getName(),
                updateUser.getBirthday(),
                id
        );
        log.info("Пользователь успешно обновлен. Измененный пользователь: {}", updateUser);
        return getUser(id);
    }
}