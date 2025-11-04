package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.LikeAddException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.*;

@Slf4j
@Repository
public class FilmDbStorage extends BaseRepository<Film> implements FilmStorage {
    @Autowired
    private GenreService genreService;
    @Autowired
    private MpaService mpaService;
    @Autowired
    private JdbcTemplate jdbc;
    private final String warnEmptyGenreInFilm = "Для фильма с ID {} жанры не найдены";

    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper) {
        super(jdbc, mapper, Film.class);
    }

    @Override
    public List<Film> getFilms() {
        String findAllQuery = "SELECT * FROM films";

        List<Film> films = findMany(findAllQuery);
        List<Genre> genres = genreService.getAllGenresInFilm();
        for (Film film : films) {
            List<Genre> filmGenres = genres.stream()
                    .filter(g -> g.getFilmId().equals(film.getId()))
                    .toList();
            updateLikesAndMpaInFilm(film);
            if (!filmGenres.isEmpty()) {
                updateGenreInFilm(film, filmGenres);
            } else {
                log.warn(warnEmptyGenreInFilm, film.getId());
            }
            genres.removeIf(filmGenres::contains);
        }
        return films;
    }

    @Override
    public Film getFilm(Long id) {
        String findByIdQuery = "SELECT * FROM PUBLIC.films WHERE id = ?";

        Optional<Film> filmOptional = findOne(findByIdQuery, id);
        filmOptional.ifPresent(this::updateLikesAndMpaInFilm);
        filmOptional.ifPresent(film -> updateGenreInFilm(film, List.of()));
        return filmOptional.orElse(null);
    }

    private void updateGenreInFilm(Film film, List<Genre> genres) {
        String findByIdGenresQuery = "SELECT DISTINCT g.id, g.name FROM GENRES_IN_FILM AS gif \n" +
                "INNER JOIN GENRES AS g ON g.ID = gif.GENRE_ID where gif.FILM_ID = ? ORDER BY g.id ASC";
        Long filmId = film.getId();
        if (genres.isEmpty()) {
            Optional<List<Genre>> genresInFilm = Optional.of(jdbc.query(findByIdGenresQuery, new GenreRowMapper(), filmId));
            if (!genresInFilm.get().isEmpty()) {
                List<Genre> genreList = genresInFilm.get();
                film.setGenres(genreList);
            } else {
                log.warn(warnEmptyGenreInFilm, film.getId());
            }
        } else {
            film.setGenres(genres);
        }
    }

    private void updateLikesAndMpaInFilm(Film film) {
        String findByIdLikesQuery = "SELECT id FROM likes WHERE film_id = ?";
        String findByIdMpaQuery = "SELECT mpa_rating_id FROM films WHERE id = ?";
        Long filmId = film.getId();
        Mpa mpa = mpaService.getMpa(jdbc.queryForObject(findByIdMpaQuery, Integer.class, filmId));
        film.setMpa(mpa);
        List<Long> likes = jdbc.query(findByIdLikesQuery, (rs, rn) -> rs.getLong(1), filmId);
        film.setLikes(new HashSet<>(likes));
    }

    @Override
    public Film createFilm(Film film) {
        String insertFilmQuery = "INSERT INTO films(id, name, description, releaseDate, " +
                "duration, mpa_rating_id)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        log.info("Получен запрос на создание фильма: {}", film);
        Long id = jdbc.queryForObject("SELECT NEXT VALUE FOR film_id_seq", Long.class);
        film.setId(id);
        for (Genre genre : film.getGenres()) {
            if (genre.getId() != null) {
                genreService.saveGenresInFilm(id, genre.getId());
            }
        }
        insert(insertFilmQuery,
                id,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        log.info("Фильм успешно создан. Созданный фильм: {}", film);
        return getFilm(id);
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        String updateQuery = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, " +
                "mpa_rating_id = ? WHERE id = ?";

        Long id = updatedFilm.getId();
        update(
                updateQuery,
                updatedFilm.getName(),
                updatedFilm.getDescription(),
                updatedFilm.getReleaseDate(),
                updatedFilm.getDuration(),
                updatedFilm.getMpa().getId(),
                id
        );
        log.info("Фильм успешно обновлен. Измененный фильм: {}", updatedFilm);
        return getFilm(id);
    }

    @Override
    public Film addLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        String insertLikesQuery = "INSERT INTO likes(film_id, user_id) values (?, ?)";

        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        if (film.getLikes().contains(userId)) {
            throw new LikeAddException("Пользователи с id " + userId + " уже добавил лайк посту с id  "
                    + filmId);
        }
        insert(insertLikesQuery,
                filmId,
                userId
        );
        log.info("Пользователь {} поставил лайк фильму {} ", userId, filmId);
        return film;
    }

    @Override
    public Film removeLike(Long filmId, Long userId) {
        Film film = getFilm(filmId);
        String deleteLikesQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        update(deleteLikesQuery,
                filmId,
                userId
        );
        log.info("Пользователь {} убрал лайк у фильма {} ", userId, filmId);
        return film;
    }

    public Set<Long> getLikes(Long filmId) {
        String sql = "SELECT user_id FROM likes WHERE film_id = ?";
        return new HashSet<>(jdbc.queryForList(sql, Long.class, filmId));
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String findPopularQuery = "select top " + count + " f.id, f.name, f.description, " +
                "f.releaseDate, f.duration, f.mpa_rating_id\n" +
                "from films as f\n" +
                "inner join likes as l\n" +
                "    on l.film_id = f.id\n" +
                "group by f.id, f.name, f.description, f.releaseDate, f.duration, f.mpa_rating_id\n" +
                "order by count(l.id) desc";

        return findMany(findPopularQuery);
    }
}