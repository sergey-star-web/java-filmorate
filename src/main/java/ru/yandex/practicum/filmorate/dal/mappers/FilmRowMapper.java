package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Slf4j
@Component
public class FilmRowMapper implements RowMapper<Film> {
    private final String FIND_BY_ID_LIKES_QUERY = "SELECT id FROM likes WHERE film_id = ?";
    private final String FIND_BY_ID_GENRES_QUERY = "SELECT DISTINCT g.id, g.name FROM GENRES_IN_FILM AS gif \n" +
            "INNER JOIN GENRES AS g ON g.ID = gif.GENRE_ID where gif.FILM_ID = ? ORDER BY g.id ASC";
    @Autowired
    private JdbcTemplate jdbc;
    @Autowired
    private MpaDbStorage mpaDbStorage;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        LocalDate releaseDate = resultSet.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        film.setReleaseDate(LocalDate.from((TemporalAccessor) releaseDate));
        film.setDuration(resultSet.getInt("duration"));
        Mpa mpa = mpaDbStorage.getMpa(resultSet.getInt("mpa_rating_id"));
        film.setMpa(mpa);
        List<Long> likes = jdbc.query(FIND_BY_ID_LIKES_QUERY, (rs, rn) -> rs.getLong(1), film.getId());
        film.setLikes(new HashSet<>(likes));
        Optional<List<Genre>> genres = Optional.of(jdbc.query(FIND_BY_ID_GENRES_QUERY, new GenreRowMapper(), film.getId()));
        if (!genres.get().isEmpty()) {
            List<Genre> genreList = genres.get();
            film.setGenres(genreList);
        } else {
            log.warn("Для фильма с ID {} жанры не найдены", film.getId());
        }
        return film;
    }
}