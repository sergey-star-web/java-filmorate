package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    @GetMapping()
    public ResponseEntity<List<Film>> getfilms() {
        if (filmService.getAllFilms().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(filmService.getAllFilms());
        }
        return ResponseEntity.ok(filmService.getAllFilms());
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
    }
}