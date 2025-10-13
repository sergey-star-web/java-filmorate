package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getFilms();
    }

    public Film updateFilm(Film updatedFilm) {
        return filmStorage.update(updatedFilm);
    }

    public Film addLike(Long filmId, Long userId) {
        return this.filmStorage.addLike(filmId, userId);
    }

    public Film removeLike(Long filmId, Long userId) {
        return this.filmStorage.addLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return this.filmStorage.getPopularFilms(count);
    }

    public Film getFilm(Long id) {
        return this.filmStorage.getFilm(id);
    }
}