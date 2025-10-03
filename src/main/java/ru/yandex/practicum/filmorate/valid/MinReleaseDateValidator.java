package ru.yandex.practicum.filmorate.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class MinReleaseDateValidator implements ConstraintValidator<MinReleaseDate, LocalDate> {
    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // или false, если null недопустимо
        }
        if (!value.isBefore(minDate)) {
            return true;
        } else {
            log.warn("Фильм не прошел валидацию по дате релиза");
            return false;
        }
    }
}
