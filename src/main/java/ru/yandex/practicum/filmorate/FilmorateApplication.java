package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

@SpringBootApplication
public class FilmorateApplication {
	public static void main(String[] args) {
		SpringApplication.run(FilmorateApplication.class, args);
		Film harryPotter = Film.builder()
				.id(1L)
				.name("Harry Potter")
				.releaseDate(LocalDate.of(2022, 1, 1))
				.duration(Duration.ofMinutes(110))
				.build();
		User dragon777 = User.builder()
				.id(12L)
				.name("Dragon777")
				.login("dragon.777")
				.email("dragon777@gmail.com")
				.birthday(LocalDate.of(2001, 8, 31))
				.build();
	}
}