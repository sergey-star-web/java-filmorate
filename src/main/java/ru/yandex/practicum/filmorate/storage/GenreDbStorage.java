package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class GenreDbStorage extends BaseRepository<Genre> implements GenreStorage {
    private final String findAllQuery = "SELECT * FROM genres";
    private final String findByIdQuery = "SELECT * FROM genres WHERE id = ?";
    private final String insertGenresInFilm = "INSERT INTO genres_in_film(film_id, genre_id) values (?, ?)";

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
        return findMany(findAllQuery);
    }

    public void saveGenresInFilm(Long filmId, Integer genreId) {
        insert(insertGenresInFilm,
                filmId,
                genreId
        );
    }
}