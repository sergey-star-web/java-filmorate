package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import ru.yandex.practicum.filmorate.serializer.DurationMinutesDeserializer;
import ru.yandex.practicum.filmorate.serializer.DurationMinutesSerializer;
import ru.yandex.practicum.filmorate.valid.MinReleaseDate;
import ru.yandex.practicum.filmorate.valid.PositiveDuration;

import java.time.Duration;
import java.time.LocalDate;
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
    @JsonSerialize(using = DurationMinutesSerializer.class)
    @JsonDeserialize(using = DurationMinutesDeserializer.class)
    @PositiveDuration
    private Duration duration;

    public void setDuration(Integer duration) {
        this.duration = Duration.ofMinutes(duration);
    }
}