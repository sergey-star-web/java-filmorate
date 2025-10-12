package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Long, Film> films = new HashMap<>();
    private Map<Long, Set<Long>> likes = new HashMap<>();
    private Long idCounter = 1L;

    public Film create(Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        film.setId(genNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан. Созданный фильм: {}", film);
        return film;
    }

    private Long genNextId() {
        return idCounter++;
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film getFilm(Long id) {
        return films.get(id);
    }

    public Film update(Film updatedFilm) {
        Long id = updatedFilm.getId();
        isNullFilm(id);
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Фильм успешно обновлен. Измененный фильм: {}", updatedFilm);
        return updatedFilm;
    }

    private Film isNullFilm(Long id) {
        Film film = films.get(id);
        if (film == null) {
            String errorMessage = String.format("Не найден фильм с %d", id);
            log.warn(errorMessage);
            throw new NotFoundException(errorMessage);
        }
        return film;
    }

    public void addLike(Long filmId, Long userId) {
        if (films.containsKey(filmId)) {
            likes.computeIfAbsent(filmId, k -> new HashSet<>()).add(userId);
        } else {
            // Обработка случая, когда фильм не найден
            throw new IllegalArgumentException("Фильм не найден");
        }
    }

    // Метод для удаления лайка у фильма
    public void removeLike(Long filmId, Long userId) {
        likes.getOrDefault(filmId, new HashSet<>()).remove(userId);
    }

    // Метод для получения списка популярных фильмов по количеству лайков
    public List<Film> getPopularFilms(Integer count) {
        // Пример реализации, где фильмы сортируются по количеству лайков и возвращаются первые 'count' фильмов
        return films.values().stream()
                .sorted((f1, f2) -> Integer.compare(countLikes(f2.getId()), countLikes(f1.getId())))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int countLikes(Long filmId) {
        return likes.getOrDefault(filmId, new HashSet<>()).size();
    }
}
