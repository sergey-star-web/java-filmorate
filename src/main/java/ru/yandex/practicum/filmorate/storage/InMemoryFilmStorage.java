package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.LikeAddException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Long, Film> films = new HashMap<>();
    private Long idCounter = 1L;
    @Autowired
    private UserStorage userStorage;

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
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(Long id) {
        return films.get(id);
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
