package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.valid.MinReleaseDate;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(of = {"id"})
public class Film {
    private Long id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Length(max = 200, message = "Максимальная длина фильма 200 символов.")
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @MinReleaseDate
    private LocalDate releaseDate;
    @JsonFormat(pattern = "MINUTES")
    private Duration duration;
}