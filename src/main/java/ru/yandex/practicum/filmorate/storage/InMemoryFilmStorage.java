package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private HashMap<Long, Film> films = new HashMap<>();
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

    public Film update(Film updatedFilm) {
        Long id = updatedFilm.getId();
        Film film = films.get(id);
        isNullFilm(film);
        films.put(updatedFilm.getId(), updatedFilm);
        log.info("Фильм успешно обновлен. Измененный фильм: {}", updatedFilm);
        return updatedFilm;
    }

    private void isNullFilm(Film film) {
        if (film == null) {
            String errorMessage = String.format("Не найден фильм с %d", film.getId());
            log.error(errorMessage);
            throw new ValidationException(errorMessage);
        }
    }
}
