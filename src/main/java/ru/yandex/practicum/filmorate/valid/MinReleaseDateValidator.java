package ru.yandex.practicum.filmorate.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.constant.Constant.formatter;

@Slf4j
public class MinReleaseDateValidator implements ConstraintValidator<MinReleaseDate, LocalDate> {
    private LocalDate minDate;

    @Override
    public void initialize(MinReleaseDate constraintAnnotation) {
        this.minDate = LocalDate.parse(constraintAnnotation.value(), formatter);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // или false, если null недопустимо
        }
        if (!value.isBefore(minDate)) {
            return true;
        } else {
            log.error("Фильм не прошел валидацию по дате релиза");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895");
        }
    }
}
