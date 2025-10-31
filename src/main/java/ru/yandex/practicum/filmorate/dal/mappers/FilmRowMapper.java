package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constant.Constant;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    private String FIND_BY_ID_LIKES_QUERY = "SELECT id FROM likes WHERE film_id = ?";
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("ID"));
        film.setName(resultSet.getString("NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        LocalDate releaseDate = resultSet.getTimestamp("RELEASEDATE").toLocalDateTime().toLocalDate();
        film.setReleaseDate(LocalDate.from((TemporalAccessor) releaseDate));
        film.setDuration(resultSet.getInt("DURATION"));
        MpaRating mpaRating = Constant.getMpaRatingById(resultSet.getInt("MPA_RATING_ID"));
        film.setMpaRating(mpaRating);

        List<Long> likes = jdbc.query(FIND_BY_ID_LIKES_QUERY, (rs, rn) -> rs.getLong(1), film.getId());
        film.setLikes(new HashSet<>(likes));

        //Set<Long> genresIds = new HashSet<>();
        //Array genresArray = resultSet.getArray("genres");
        //if (genresArray != null) {
        //    Object arrayObj = genresArray.getArray();
        //    Long[] genresLongArray = (Long[]) arrayObj;
        //    genresIds = new HashSet<>(Arrays.asList(genresLongArray));
        //}
        //Set<Genre> genres = new HashSet<>();
        //for (Long genreId : genresIds) {
        //    Genre genre =  Constant.getGenreById(genreId);
        //    genres.add(genre);
        //}
        //film.setGenres(genres);

        return film;
    }
}
