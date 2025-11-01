package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.valid.MinReleaseDate;

import java.time.LocalDate;
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
    private Set<Long> likes;
    private List<Genre> genres;

    public Film() {
        likes = new HashSet<>();
    }

    public boolean addLike(Long id) {
        return likes.add(id);
    }

    public boolean removeLike(Long id) {
        return likes.remove(id);
    }

    public boolean addGenre(Genre genre) {
        return genres.add(genre);
    }

    public boolean removeGenre(Genre genre) {
        return genres.remove(genre);
    }
}