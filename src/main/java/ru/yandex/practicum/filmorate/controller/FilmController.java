package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private HashMap<Long, Film> films = new HashMap<>();
    private Long idCounter = 1L;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос на создание фильма: {}", film);
        film.setId(genNextId());
        films.put(film.getId(), film);
        log.info("Фильм успешно создан. Созданный фильм: {}", film);
        return film;
    }

    private Long genNextId() {
        return idCounter++;
    }

    @GetMapping()
    public List<Film> getfilms() {
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film updatedFilm) {
        Long id = updatedFilm.getId();
        Film film = films.get(id);
        isNullFilm(film);
        films.put(updatedFilm.getId(), updatedFilm);
        return updatedFilm;
    }

    private void isNullFilm(Film film) {
        if (film == null) {
            String errorMessage = String.format("Не найден фильм с %d", film.getId());
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }
}