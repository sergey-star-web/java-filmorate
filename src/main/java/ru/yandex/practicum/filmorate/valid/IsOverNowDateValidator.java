package ru.yandex.practicum.filmorate.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class IsOverNowDateValidator implements ConstraintValidator<IsOverNowDate, LocalDate> {
    private LocalDate date = LocalDate.now();

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // или false, если null недопустимо
        }
        if (!value.isAfter(date)) {
            return true;
        } else {
            log.error("Пользователь не прошел валидацию по дате рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}