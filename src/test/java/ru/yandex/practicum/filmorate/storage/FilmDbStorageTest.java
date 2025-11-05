package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.config.TestConfig;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.*;

@JdbcTest
@Import(TestConfig.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbc;
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    Mpa pg = new Mpa(2, "PG");
    Mpa nc17 = new Mpa(5, "NC-17");
    Genre comedy = new Genre(1, "COMEDY");
    Genre drama = new Genre(2, "DRAMA");
    Genre science = new Genre(3, "CARTOON");

    private final String name = "test film";
    private final String description = "test desc";

    @BeforeEach
    void setUp() {
        jdbc.update("DELETE FROM likes");
        jdbc.update("DELETE FROM genres_in_film");
        jdbc.update("DELETE FROM films");
        jdbc.update("DELETE FROM friends");
        jdbc.update("DELETE FROM users");
        jdbc.update("ALTER SEQUENCE film_id_seq RESTART WITH 1");
        jdbc.update("ALTER SEQUENCE user_id_seq RESTART WITH 1");
    }

    private Film createTestFilm(String name, String description, Mpa mpa) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(90);
        film.setMpa(mpa);
        return film;
    }

    @Test
    void testCreateFilm() {
        Film film = createTestFilm(name, description, pg);
        filmDbStorage.createFilm(film);
        Film savedFilm = filmDbStorage.getFilm(film.getId());
        assertThat(savedFilm).isNotNull();
        assertThat(savedFilm.getId()).isEqualTo(1L);
        assertThat(savedFilm.getName()).isEqualTo(name);
        assertThat(savedFilm.getDescription()).isEqualTo(description);
        assertThat(savedFilm.getMpa().getId()).isEqualTo(pg.getId());
        assertThat(savedFilm.getDuration()).isEqualTo(90);
        assertThat(savedFilm.getReleaseDate()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(savedFilm.getLikes()).isEmpty();
    }

    @Test
    void testCreateFilmWithGenres() {
        Film film = createTestFilm(name, description, pg);
        List<Genre> genres = Arrays.asList(comedy, drama);
        film.setGenres(genres);
        filmDbStorage.createFilm(film);
        Film savedFilm = filmDbStorage.getFilm(film.getId());
        List<Integer> genresIds = savedFilm.getGenres().stream().map(Genre::getId).toList();
        assertThat(genresIds).hasSize(2);
        assertThat(genresIds).contains(comedy.getId(), drama.getId());
    }

    @Test
    void testGetFilm() {
        Film film = createTestFilm(name, description, pg);
        filmDbStorage.createFilm(film);
        Long filmId = film.getId();
        Film foundFilm = filmDbStorage.getFilm(filmId);
        assertThat(foundFilm).isNotNull();
        assertThat(foundFilm.getId()).isEqualTo(filmId);
        assertThat(foundFilm.getName()).isEqualTo(name);
    }

    @Test
    void testGetFilmNotFound() {
        Film foundFilm = filmDbStorage.getFilm(45745756L);
        assertThat(foundFilm).isNull();
    }

    @Test
    void testGetAllFilms() {
        Film film1 = createTestFilm("first film", "desc 111", pg);
        Film film2 = createTestFilm("second film", "desc 222", nc17);
        filmDbStorage.createFilm(film1);
        filmDbStorage.createFilm(film2);
        List<Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(2);
        assertThat(films.getFirst().getName()).isEqualTo("first film");
        assertThat(films.getLast().getName()).isEqualTo("second film");
    }

    @Test
    void testAddAndRemoveLike() {
        Film film = createTestFilm(name, description, pg);
        filmDbStorage.createFilm(film);
        Long filmId = film.getId();
        User user = new User();
        user.setEmail("example@mail.ru");
        user.setLogin("user");
        user.setName("test user");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        userDbStorage.createUser(user);
        Long userId = user.getId();
        filmDbStorage.addLike(userId, filmId);
        Set<Long> likes = filmDbStorage.getLikes(filmId);
        assertThat(likes).contains(userId);
        filmDbStorage.removeLike(userId, filmId);
        likes = filmDbStorage.getLikes(filmId);
        assertThat(likes).doesNotContain(userId);
    }

    @Test
    void testFilmWithMultipleGenres() {
        Film film = createTestFilm(name, description, pg);
        List<Genre> genres = Arrays.asList(comedy, drama, science);
        film.setGenres(genres);
        filmDbStorage.createFilm(film);
        Film savedFilm = filmDbStorage.getFilm(film.getId());
        List<Integer> genresIds = savedFilm.getGenres().stream().map(Genre::getId).toList();
        assertThat(genresIds).hasSize(3);
        assertThat(genresIds).contains(comedy.getId(), drama.getId(), science.getId());
    }
}