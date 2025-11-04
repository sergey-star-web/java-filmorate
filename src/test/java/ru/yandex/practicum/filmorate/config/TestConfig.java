package ru.yandex.practicum.filmorate.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import javax.sql.DataSource;

@TestConfiguration
public class TestConfig {
    @Bean
    public UserDbStorage userDbStorage(DataSource dataSource) {
        return new UserDbStorage(new JdbcTemplate(dataSource), new UserRowMapper());
    }

    @Bean
    public FilmDbStorage filmDbStorage(DataSource dataSource) {
        return new FilmDbStorage(new JdbcTemplate(dataSource), new FilmRowMapper());
    }

    @Bean
    public GenreDbStorage genreDbStorage(DataSource dataSource) {
        return new GenreDbStorage(new JdbcTemplate(dataSource), new GenreRowMapper());
    }

    @Bean
    public MpaDbStorage mpaDbStorage(DataSource dataSource) {
        return new MpaDbStorage(new JdbcTemplate(dataSource), new MpaRowMapper());
    }
}