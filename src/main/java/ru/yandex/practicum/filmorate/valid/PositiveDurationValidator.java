package ru.yandex.practicum.filmorate.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class PositiveDurationValidator implements ConstraintValidator<PositiveDuration, Duration> {
    private final Duration durationMin = Duration.ofSeconds(0);

    @Override
    public boolean isValid(Duration value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // или false, если null недопустимо
        }
        if (value.toMinutes() >= this.durationMin.toMinutes()) {
            return true;
        } else {
            log.warn("Фильм не прошел валидацию по продолжительности");
            return false;
        }
    }
}
