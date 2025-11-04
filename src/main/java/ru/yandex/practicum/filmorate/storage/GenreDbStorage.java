package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreInFilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private final String findByIdQuery = "SELECT * FROM genres WHERE id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper, Genre.class);
    }

    public boolean exists(Integer id) {
        Genre genre = findOne(findByIdQuery, id).orElse(null);
        return genre != null;
    }

    public Genre getGenre(Integer id) {
        Optional<Genre> genreOptional = findOne(findByIdQuery, id);
        if (genreOptional.isPresent()) {
            return genreOptional.get();
        } else {
            throw new NotFoundException("Жанр с id " + id + " не найден");
        }
    }

    public List<Genre> getAllGenres() {
        String findAllQuery = "SELECT * FROM genres";
        return findMany(findAllQuery);
    }

    public List<Genre> getAllGenresInFilm() {
        String findAllQuery = "SELECT DISTINCT g.id, g.name, gif.film_id FROM GENRES_IN_FILM AS gif \n" +
                "INNER JOIN GENRES AS g ON g.ID = gif.GENRE_ID ORDER BY g.id ASC";
        return jdbc.query(findAllQuery, new GenreInFilmRowMapper());
    }

    public void saveGenresInFilm(Long filmId, Integer genreId) {
        String insertGenresInFilm = "INSERT INTO genres_in_film(film_id, genre_id) values (?, ?)";
        insert(insertGenresInFilm,
                filmId,
                genreId
        );
    }
}