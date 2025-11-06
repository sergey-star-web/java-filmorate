package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAccessor;

@Slf4j
@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();

        film.setId(resultSet.getLong("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        LocalDate releaseDate = resultSet.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        film.setReleaseDate(LocalDate.from((TemporalAccessor) releaseDate));
        film.setDuration(resultSet.getInt("duration"));
        film.setMpaId(resultSet.getInt("mpa_rating_id"));
        return film;
    }
}