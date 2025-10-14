package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmTests {
    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void afterAll() {
        factory.close();
    }

    private Film createFilm() {
        Film film = Film.builder()
                .name("film")
                .description("film description")
                .releaseDate(LocalDate.now())
                .duration(120)
                .build();
        return film;
    }

    @Test
    void testBlankName() {
        Film film = createFilm();
        film.setName("");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Название фильма не может быть пустым.", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testMaxLenDescription() {
        Film film = createFilm();
        film.setDescription("rhn908retj8hert0hje-rtjherjh09we45j0h[jw490hj904wejh90-=w4e5hj4w-9jh-459jh-w4wjh49hwerj" +
                "rtohnirstopihnpiorstn[hnsrtnh[isrtnp[rsdptn[prtsjmnpsrjmphjkrspthk-rwoptjpnaep'rgjgae-[orjgpaemjeap" +
                "eargoaerhiopaes0rhjaejh-]aejhp[rtjs-hjmsr-thk[erhjipeorhjneorjhejarhpi[eajrhjearhjoperujhpjearhjepr" +
                "aerlkhnaerhioaep'rjhpaejnrgpnmaepohnaepirjhpaejrhpaejrhp-aejphraj0phjae9-pjhpaejrhpeajrhpae-aerhj");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Максимальная длина фильма 200 символов.", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testFilmMinReleaseDate() {
        Film film = createFilm();
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Дата релиза должна быть не раньше 28 декабря 1895", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testMinDurationFilm() {
        Film film = createFilm();
        film.setDuration(-1);

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Продолжительность фильма должна быть положительным числом", violations.iterator().next().getMessageTemplate());
    }
}
