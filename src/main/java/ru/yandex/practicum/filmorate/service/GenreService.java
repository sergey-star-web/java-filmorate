package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    @Autowired
    private GenreStorage genreStorage;

    public Genre getGenre(Integer id) {
        return genreStorage.getGenre(id);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public List<Genre> getAllGenresInFilm() {
        return genreStorage.getAllGenresInFilm();
    }

    public void saveGenresInFilm(Long filmId, Integer genreId) {
        genreStorage.saveGenresInFilm(filmId, genreId);
    }
}