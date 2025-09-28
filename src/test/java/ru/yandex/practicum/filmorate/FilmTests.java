package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
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
                .duration(Duration.ofMinutes(120))
                .build();
        return film;
    }

    @Test
    void testBlankName() {
        Film film = createFilm();
        film.setName("");

        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        System.out.println(violations.iterator().next().getMessageTemplate());
        assertEquals("Название фильма не может быть пустым.", violations.iterator().next().getMessageTemplate());
    }
}
