package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RequestMapping("/films")
@Slf4j
@RestController
public class FilmController {
    @Autowired
    private FilmService filmService;
    @Autowired
    private MpaService mpaService;
    @Autowired
    private GenreService genreService;

    @GetMapping
    public ResponseEntity<List<Film>> getFilms() {
        return ResponseEntity.ok(filmService.getAllFilms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getFilm(@PathVariable Long id) {
        return ResponseEntity.ok(filmService.getFilm(id));
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        // Проверка существования рейтинга Mpa
        Integer mpaId = film.getMpa().getId();
        if (!mpaService.exists(mpaId)) {
            String errorMessage = String.format("Не найден рейтинг с ID %d", mpaId);
            log.warn(errorMessage);
            throw new NotFoundException();
        }
        List<Integer> genres = genreService.getAllGenres().stream()
                .map(Genre::getId)
                .toList();
        // Проверка соответствия жанров
        for (Genre genre : film.getGenres()) {
            Integer genreId = genre.getId();
            if (!genres.contains(genreId)) {
                String errorMessage = String.format("Не найден жанр с ID %d", genreId);
                log.warn(errorMessage);
                throw new NotFoundException();
            }
        }
        return filmService.createFilm(film);
    }

    @PutMapping
    public ResponseEntity<Film> update(@Valid @RequestBody Film updatedFilm) {
        Film film = filmService.getFilm(updatedFilm.getId());
        if (film == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updatedFilm);
        }
        return ResponseEntity.ok(filmService.updateFilm(updatedFilm));
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return ResponseEntity.ok(filmService.getPopularFilms(count));
    }
}