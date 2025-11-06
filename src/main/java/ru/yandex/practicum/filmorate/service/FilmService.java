package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private MpaService mpaService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private JdbcTemplate jdbc;
    private final String warnEmptyGenreInFilm = "Для фильма с ID {} жанры не найдены";

    public Film createFilm(Film film) {
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
        filmStorage.createFilm(film);
        // сохраняем жанры в промежуточную таблицу
        for (Genre genre : film.getGenres()) {
            if (genre.getId() != null) {
                genreService.saveGenresInFilm(film.getId(), genre.getId());
            }
        }
        return getFilm(film.getId());
    }

    private List<Long> getLikesInFim(Long filmId) {
        String findByIdLikesQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbc.query(findByIdLikesQuery, (rs, rn) -> rs.getLong(1), filmId);
    }

    private HashMap<Long, Long> getAllLikes() {
        String findByIdLikesQuery = "SELECT user_id, film_id FROM likes";
        HashMap<Long, Long> likesMap = new HashMap<>();
        jdbc.query(findByIdLikesQuery, (rs, rn) -> {
            Long userId = rs.getLong("user_id");
            Long filmId = rs.getLong("film_id");
            likesMap.put(userId, filmId);
            return likesMap; // Возврат карты для каждого ряда не требуется, но нужен для соответствия сигнатуре метода
        });
        return likesMap;
    }

    private void updateLikesInFilm(Film film, List<Long> likes) {
        film.setLikes(new HashSet<>(likes));
    }

    private void updateMpaInFilm(Film film) {
        Mpa mpa = mpaService.getMpa(film.getMpa().getId());
        film.setMpa(mpa);
    }

    private void updateGenreInFilm(Film film, List<Genre> genreInFilm) {
        if (!genreInFilm.isEmpty()) {
            film.setGenres(genreInFilm);
        } else {
            log.warn(warnEmptyGenreInFilm, film.getId());
        }
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getFilms();
        List<Genre> genres = genreService.getAllGenresInFilm();
        HashMap<Long, Long> likes = getAllLikes();
        for (Film film : films) {
            // Получаем жанры конкретного фильма
            List<Genre> genreInFilm = genres.stream()
                            .filter(g -> g.getFilmId().equals(film.getId()))
                            .toList();
            genres.removeIf(genreInFilm::contains);
            // Получаем лайки конкретного фильма
            List<Long> likesInFilm = likes.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(film.getId()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            likes.entrySet().removeIf(entry -> likesInFilm.contains(entry.getKey()));
            updateMpaInFilm(film);
            updateLikesInFilm(film, likesInFilm);
            updateGenreInFilm(film, genreInFilm);
        }
        return films;
    }

    public Film updateFilm(Film updatedFilm) {
        Film film = filmStorage.getFilm(updatedFilm.getId());
        if (film == null) {
            throw new NotFoundException();
        }
        return filmStorage.updateFilm(updatedFilm);
    }

    public Film addLike(Long filmId, Long userId) {
        return filmStorage.addLike(filmId, userId);
    }

    public Film removeLike(Long filmId, Long userId) {
        return filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

    public Film getFilm(Long id) {
        Film film = filmStorage.getFilm(id);
        if (film != null) {
            updateMpaInFilm(film);
            List<Genre> genreInFilm = genreService.getGenresInFilm(id);
            updateGenreInFilm(film, genreInFilm);
            updateLikesInFilm(film, getLikesInFim(id));
            return film;
        }
        return null;
    }
}