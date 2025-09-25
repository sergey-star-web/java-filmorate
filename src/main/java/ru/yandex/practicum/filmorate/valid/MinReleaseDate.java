package ru.yandex.practicum.filmorate.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MinReleaseDateValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MinReleaseDate {
    String message() default "Дата релиза должна быть не раньше {value}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String value() default "1895-12-28"; // минимальная дата в формате "yyyy-MM-dd"
}
