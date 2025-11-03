package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RequestMapping("/mpa")
@Slf4j
@RestController
public class MpaController {
    @Autowired
    private MpaService mpaService;

    @GetMapping
    public ResponseEntity<List<Mpa>> getGenres() {
        return ResponseEntity.ok(mpaService.getAllMpa());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getGenre(@PathVariable Integer id) {
        return ResponseEntity.ok(mpaService.getMpa(id));
    }
}