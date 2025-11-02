package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.Constant;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.HashSet;
import java.util.List;

@Component
public class UserRowMapper implements RowMapper<User> {
    private final String FIND_BY_ID_LIKES_QUERY = "SELECT id FROM likes WHERE film_id = ?";
    private final String FIND_BY_ID_GENRES_QUERY = "SELECT g.id, g.name FROM GENRES_IN_FILM AS gif \n" +
            "INNER JOIN GENRES AS g ON g.ID = gif.GENRE_ID where gif.FILM_ID = ?";
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    MpaDbStorage mpaDbStorage;

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();

        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        LocalDate birthday = LocalDate.parse(resultSet.getTimestamp("birthday").toLocalDateTime().toLocalDate().format(Constant.formatter));
        user.setBirthday(birthday);
        return user;
    }
}
