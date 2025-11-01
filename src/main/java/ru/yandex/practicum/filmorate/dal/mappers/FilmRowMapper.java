package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.*;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private final String FIND_BY_ID_LIKES_QUERY = "SELECT id FROM likes WHERE film_id = ?";
    private final String FIND_BY_ID_GENRES_QUERY = "SELECT g.const FROM GENRES_IN_FILM AS gif \n" +
            "INNER JOIN GENRES AS g ON g.ID = gif.GENRE_ID where gif.FILM_ID = ?";
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        List<Genre> genres = new ArrayList<>();

        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        LocalDate releaseDate = resultSet.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        film.setReleaseDate(LocalDate.from((TemporalAccessor) releaseDate));
        film.setDuration(resultSet.getInt("duration"));
        //film.setMpaRating(MpaRating.valueOf(resultSet.getString("mpa")));
        List<Long> likes = jdbc.query(FIND_BY_ID_LIKES_QUERY, (rs, rn) -> rs.getLong(1), film.getId());
        film.setLikes(new HashSet<>(likes));
        //List<String> queryResults = jdbc.query(FIND_BY_ID_GENRES_QUERY, new Object[]{film.getId()}, (rs, rn) -> rs.getString("const"));
        //for (String genreConst : queryResults) {
        //    Genre genre = Genre.valueOf(genreConst);
        //    genres.add(genre);
        //}
        //film.setGenres(new HashSet<>(genres));
        return film;
    }
}
