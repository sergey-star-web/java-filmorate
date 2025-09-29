package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.yandex.practicum.filmorate.valid.IsOverNowDate;

import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.constant.Constant.dateTimeFormatString;

@Data
@Builder
@EqualsAndHashCode(of = {"id"})
public class User {
    private Long id;
    @NotBlank(message = "Почта не может быть пустым.")
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^\\S+$", message = "Логин не должен содержать пробелов")
    private String login;
    @Getter
    private String name;
    @JsonFormat(pattern = dateTimeFormatString)
    @IsOverNowDate
    private LocalDate birthday;

    public String getName() {
        if (name == null) {
            return this.login;
        }
        return this.name;
    }
}