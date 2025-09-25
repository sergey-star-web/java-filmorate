package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(of = {"id"})
public class User {
    private Long id;
    @NotBlank(message = "Почта не может быть пустым.")
    @Email
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    @Pattern(regexp = "^[^\\s]+$", message = "Логин не должен содержать пробелов")
    private String login;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}