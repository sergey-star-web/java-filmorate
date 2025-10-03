package ru.yandex.practicum.filmorate.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class IsOverNowDateValidator implements ConstraintValidator<IsOverNowDate, LocalDate> {
    private final LocalDate date = LocalDate.now();

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // или false, если null недопустимо
        }
        if (!value.isAfter(date)) {
            return true;
        } else {
            log.warn("Пользователь не прошел валидацию по дате рождения");
            return false;
        }
    }
}