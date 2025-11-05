package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Genre {
    private Integer id;
    private String name;
    private Long filmId;

    public Genre() {

    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}