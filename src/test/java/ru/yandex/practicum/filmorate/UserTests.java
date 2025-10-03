package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTests {
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

    private User createUser() {
        User user = User.builder()
                .id(12L)
                .name("Dragon777")
                .login("dragon.777")
                .email("dragon777@gmail.com")
                .birthday(LocalDate.of(2001, 8, 31))
                .build();
        return user;
    }

    @Test
    void testValidEmail() {
        User user = createUser();
        user.setEmail("dragon777 @gmail.com");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", violations.iterator().next().getMessageTemplate());
        user.setEmail("dragon777 gmail.com");
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testValidLogin() {
        User user = createUser();
        user.setLogin("dragon 777");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Логин не должен содержать пробелов", violations.iterator().next().getMessageTemplate());
        user.setLogin(null);
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым.", violations.iterator().next().getMessageTemplate());
    }

    @Test
    void testGetName() {
        User user = createUser();
        user.setName(null);

        assertEquals(user.getLogin(), user.getName(), "Имя должно быть равно логину, так как имя пустое");
    }

    @Test
    void testBirthdayInFuture() {
        User user = createUser();
        user.setBirthday(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessageTemplate());
    }
}
