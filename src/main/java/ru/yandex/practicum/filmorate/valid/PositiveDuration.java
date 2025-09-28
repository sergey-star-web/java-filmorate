package ru.yandex.practicum.filmorate.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveDurationValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveDuration {
    String message() default "Duration должно быть положительным числом";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value() default "PT0S";
}
