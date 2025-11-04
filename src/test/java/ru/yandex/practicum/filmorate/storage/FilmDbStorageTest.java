package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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

    @BeforeEach
    void setUp() {
        jdbc.update("DELETE FROM likes");
        jdbc.update("DELETE FROM genres_in_film");
        jdbc.update("DELETE FROM films");
        jdbc.update("DELETE FROM friends");
        jdbc.update("DELETE FROM users");
        //jdbc.update("DELETE FROM genres");
        //jdbc.update("DELETE FROM mpa_rating");
        //jdbc.update("ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1");
        //jdbc.update("ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1");
    }

    @Test
    void testAddFilm() {
        Film film = createTestFilm("Test Film", "Test Description", pg);
        System.out.println("aaaaa1: " + film);
        filmDbStorage.createFilm(film);
        Film savedFilm = filmDbStorage.getFilm(film.getId());
        assertThat(savedFilm).isNotNull();
        assertThat(savedFilm.getId()).isEqualTo(1L);
        assertThat(savedFilm.getName()).isEqualTo("Test Film");
        assertThat(savedFilm.getDescription()).isEqualTo("Test Description");
        assertThat(savedFilm.getMpa()).isEqualTo(pg);
        assertThat(savedFilm.getDuration()).isEqualTo(120);
        assertThat(savedFilm.getReleaseDate()).isEqualTo(LocalDate.of(2000, 1, 1));
        assertThat(savedFilm.getLikes()).isEmpty();
    }

    @Test
    void testAddFilmWithGenres() {
        Film film = createTestFilm("Test Film", "Test Description", pg);
        System.out.println("aaaaa1: " + film);
        List<Genre> genres = Arrays.asList(comedy, drama);
        film.setGenres(genres);
        filmDbStorage.createFilm(film);
        Film savedFilm = filmDbStorage.getFilm(film.getId());
        assertThat(savedFilm.getGenres()).hasSize(2);
        assertThat(savedFilm.getGenres()).contains(comedy, drama);
    }

    @Test
    void testGetFilm() {
        Film film = createTestFilm("Test Film", "Test Description", pg);
        filmDbStorage.createFilm(film);
        Long filmId = film.getId();
        Film foundFilm = filmDbStorage.getFilm(filmId);
        assertThat(foundFilm).isNotNull();
        assertThat(foundFilm.getId()).isEqualTo(filmId);
        assertThat(foundFilm.getName()).isEqualTo("Test Film");
    }

    @Test
    @DisplayName("Поиск несуществующего фильма")
    void testGetFilm_NotFound() {
        Film foundFilm = filmDbStorage.getFilm(45745756L);
        assertThat(foundFilm).isNull();
    }

    @Test
    @DisplayName("Получение всех фильмов")
    void testGetAllFilms() {
        Film film1 = createTestFilm("Film One", "Description One", pg);
        Film film2 = createTestFilm("Film Two", "Description Two", nc17);
        filmDbStorage.createFilm(film1);
        filmDbStorage.createFilm(film2);
        List<Film> films = filmDbStorage.getFilms();
        assertThat(films).hasSize(2);
        assertThat(films.getFirst().getName()).isEqualTo("Film One");
        assertThat(films.getLast().getName()).isEqualTo("Film Two");
    }

    @Test
    @DisplayName("Добавление и удаление лайка")
    void testAddAndRemoveLike() {
        Film film = createTestFilm("Test Film", "Test Description", pg);
        filmDbStorage.createFilm(film);
        Long filmId = film.getId();
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("userlogin");
        user.setName("Test User");
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
    @DisplayName("Добавление фильма с несколькими жанрами")
    void testFilmWithMultipleGenres() {
        Film film = createTestFilm("Test Film", "Test Description", pg);
        List<Genre> genres = Arrays.asList(comedy, drama, science);
        film.setGenres(genres);
        filmDbStorage.createFilm(film);
        Film savedFilm = filmDbStorage.getFilm(film.getId());
        assertThat(savedFilm.getGenres()).hasSize(3);
        assertThat(savedFilm.getGenres())
                .contains(comedy, drama, science);
    }

    private Film createTestFilm(String name, String description, Mpa mpa) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setMpa(mpa);
        return film;
    }
}