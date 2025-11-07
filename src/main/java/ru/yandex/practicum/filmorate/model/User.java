package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import ru.yandex.practicum.filmorate.valid.IsOverNowDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.constant.Constant.dateTimeFormatString;

@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
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
    private Set<Long> friends;

    public String getName() {
        if (name == null) {
            return this.login;
        }
        return this.name;
    }

    public User() {
        friends = new HashSet<>();
    }
}