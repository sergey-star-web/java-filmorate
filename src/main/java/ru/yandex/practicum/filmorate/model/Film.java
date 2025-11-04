package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.valid.MinReleaseDate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.constant.Constant.dateTimeFormatString;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
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
    private Mpa mpa;
    private List<Genre> genres;
    private Set<Long> likes;

    public Film() {
        genres = new ArrayList<>();
        likes = new HashSet<>();
    }
}