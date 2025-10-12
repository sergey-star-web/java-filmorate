package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.valid.MinReleaseDate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static ru.yandex.practicum.filmorate.constant.Constant.dateTimeFormatString;

@Data
@Builder
@EqualsAndHashCode(of = {"id"})
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Length(max = 200, message = "Максимальная длина фильма 200 символов.")
    private String description;
    @JsonFormat(pattern = dateTimeFormatString)
    @MinReleaseDate
    private LocalDate releaseDate;
    @Min(value = 0, message = "Продолжительность фильма должна быть положительным числом")
    private Integer duration;
}