package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.LikeAddException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private final String findAllQuery = "SELECT * FROM films";
    private final String findByIdQuery = "SELECT * FROM PUBLIC.films WHERE id = ?";
    private final String insertFilmQuery = "INSERT INTO films(id, name, description, releaseDate, " +
            "duration, mpa_rating_id)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private final String insertLikesQuery = "INSERT INTO likes(film_id, user_id) values (?, ?)";
    private final String updateQuery = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, " +
            "mpa_rating_id = ? WHERE id = ?";
    private final String deleteLikesQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    @Autowired
    private GenreDbStorage genreDbStorage;
    @Autowired
    private JdbcTemplate jdbc;

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public List<Film> getFilms() {
        return findMany(findAllQuery);
    }

    @Override
    public Film getFilm(Long id) {
        Optional<Film> filmOptional = findOne(findByIdQuery, id);
        return filmOptional.orElse(null);
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        Long id = jdbc.queryForObject("SELECT NEXT VALUE FOR film_id_seq", Long.class);
        film.setId(id);
        for (Genre genre : film.getGenres()) {
            genreDbStorage.saveGenresInFilm(id, genre.getId());
        }
        insert(insertFilmQuery,
                id,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        log.info("Фильм успешно создан. Созданный фильм: {}", film);
        return getFilm(id);
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        Long id = updatedFilm.getId();
        update(
                updateQuery,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getMpa().getId(),
                id
        );
        log.info("Фильм успешно обновлен. Измененный фильм: {}", updatedFilm);
        return getFilm(id);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);

        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (film.getLikes().contains(userId)) {
            throw new LikeAddException("Пользователи с id " + userId + " уже добавил лайк посту с id  "
                    + filmId);
        }
        insert(insertLikesQuery,
                filmId,
                userId
        );
        log.info("Пользователь {} поставил лайк фильму {} ", userId, filmId);
        return film;
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);

        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        update(deleteLikesQuery,
                filmId,
                userId
        );
        log.info("Пользователь {} убрал лайк у фильма {} ", userId, filmId);
        return film;
    }

    @Override
    // Метод для получения списка популярных фильмов по количеству лайков
    public List<Film> getPopularFilms(Integer count) {
        String findPopularQuery = "select top " + count + " f.id, f.name, f.description, " +
                "f.releaseDate, f.duration, f.mpa_rating_id\n" +
                "from films as f\n" +
                "inner join likes as l\n" +
                "    on l.film_id = f.id\n" +
                "group by f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_rating_id\n" +
                "order by count(l.id) desc";
        return findMany(findPopularQuery);
    }
}