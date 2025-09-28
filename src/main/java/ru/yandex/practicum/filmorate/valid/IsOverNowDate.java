package ru.yandex.practicum.filmorate.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.time.LocalDate;

@Documented
@Constraint(validatedBy = IsOverNowDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IsOverNowDate {
    String message() default "Дата не может быть в будущем {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}