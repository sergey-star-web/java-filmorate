package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.FriendsAddException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserDbStorage extends BaseRepository<User> implements UserStorage {
    private final String FIND_ALL_QUERY = "SELECT * FROM users";
    private final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private final String INSERT_USER_QUERY = "INSERT INTO users(id, email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private HashMap<Long, User> users = new HashMap<>();
    private Long idCounter = 1L;
    @Autowired
    MpaDbStorage mpaDbStorage;
    @Autowired
    GenreDbStorage genreDbStorage;
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
        insert(INSERT_USER_QUERY,
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
        //User friend = users.get(friendId);

        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найдено");
        }
        //if (friend == null) {
        //    throw new NotFoundException("Пользователь с id " + friendId + " не найдено");
        //}
        //if (user.getFriends().containsKey(friendId) || friend.getFriends().containsKey(userId)) {
        //    throw new FriendsAddException("Пользователи с id " + userId + " и " + friendId +
        //            " уже в друзьях друг у друга");
        //}
        user.addFriend(friendId);
        //friend.addFriend(userId);
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
        if (!user.getFriends().containsKey(friendId) || !friend.getFriends().containsKey(userId)) {
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
        return user.getFriends().keySet().stream()
                .map(users::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
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
        Set<Long> commonFriendIds = new HashSet<>(user.getFriends().keySet());
        commonFriendIds.retainAll(otherUser.getFriends().keySet());
        return commonFriendIds.stream()
                .map(users::get)
                .collect(Collectors.toSet());
    }

    @Override
    public List<User> getUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public User getUser(Long id) {
        Optional<User> filmOptional = findOne(FIND_BY_ID_QUERY, id);
        return filmOptional.orElse(null);
    }

    @Override
    public User updateUser(User updateUser) {
        Long id = updateUser.getId();
        update(
                UPDATE_QUERY,
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
