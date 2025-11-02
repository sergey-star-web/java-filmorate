package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

@Slf4j
@Repository
public class GenreDbStorage extends BaseRepository<Genre> {
    public GenreDbStorage(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper, Genre.class);
    }
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE id = ?";
    private static final String INSERT_GENRES_IN_FILN_QUERY = "INSERT INTO genres_in_film(film_id, genre_id) values (?, ?)";

    public boolean exists(Integer id) {
        Genre genre = findOne(FIND_BY_ID_QUERY, id).orElse(null);
        return genre != null;
    }

    public Genre getMpa(Integer id) {
        Optional<Genre> genreOptional = findOne(FIND_BY_ID_QUERY, id);
        return genreOptional.orElse(null);
    }

    public void saveGenresInFilm(Long filmId, Integer genreId) {
        insert(INSERT_GENRES_IN_FILN_QUERY,
                filmId,
                genreId
        );
    }
}
