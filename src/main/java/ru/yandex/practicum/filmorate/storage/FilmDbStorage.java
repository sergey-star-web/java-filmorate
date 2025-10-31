package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.LikeAddException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constant.Constant.formatter;

@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM PUBLIC.films WHERE id = ?";
    private HashMap<Long, Film> films = new HashMap<>();
    private Long idCounter = 1L;
    @Autowired
    @Qualifier("inMemoryUserStorage")
    private UserStorage userStorage;

    @Autowired
    private JdbcTemplate jdbc;

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public List<Film> getFilms() {
        String FIND_BY_ID_LIKES_QUERY = "SELECT id FROM likes WHERE film_id = ";
        System.out.println("AAAA1: ");
        FIND_BY_ID_LIKES_QUERY = FIND_BY_ID_LIKES_QUERY + 2;
        System.out.println("AAAA2: " + FIND_BY_ID_LIKES_QUERY);
        RowMapper<Integer> longMapper = (rs, rowNum) -> rs.getInt(1);
        System.out.println("AAAA3: " + jdbc.query(FIND_BY_ID_LIKES_QUERY, longMapper).size());
        List<Integer> likes = jdbc.query(FIND_BY_ID_LIKES_QUERY, longMapper);
        System.out.println(likes);
        return findMany(FIND_ALL_QUERY);
    }

    @Override
    public Film getFilm(Long id) {
        Optional<Film> filmOptional = findOne(FIND_BY_ID_QUERY, id);
        return filmOptional.orElse(null);
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        film.setId(genNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан. Созданный фильм: {}", film);
        return film;
    }

    private Long genNextId() {
        return idCounter++;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        Long id = updatedFilm.getId();
        if (id != null) {
            throwIfNoFilm(id);
        }
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Фильм успешно обновлен. Измененный фильм: {}", updatedFilm);
        return updatedFilm;
    }

    private void throwIfNoFilm(Long id) {
        if (!films.containsKey(id)) {
            String errorMessage = String.format("Не найден фильм с %d", id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = films.get(filmId);
        User user = userStorage.getUser(userId);

        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (film.getLikes().contains(userId)) {
            throw new LikeAddException("Пользователи с id " + userId + " уже добавил лайк посту с id  "
                    + filmId);
        }
        film.addLike(userId);
        log.info("Пользователь {} поставил лайк фильму {} ", userId, filmId);
        return film;
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        Film film = films.get(filmId);
        User user = userStorage.getUser(userId);

        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        if (!film.getLikes().contains(userId)) {
            throw new LikeAddException("Пользователи с id " + userId + " не добавлял лайк посту с id  "
                    + filmId);
        }
        film.removeLike(userId);
        log.info("Пользователь {} убрал лайк у фильма {} ", userId, filmId);
        return film;
    }

    @Override
    // Метод для получения списка популярных фильмов по количеству лайков
    public List<Film> getPopularFilms(Integer count) {
        // Пример реализации, где фильмы сортируются по количеству лайков и возвращаются первые 'count' фильмов
        return films.values().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
