package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    @Autowired
    private MpaStorage mpaStorage;

    public Mpa getMpa(Integer id) {
        return mpaStorage.getMpa(id);
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}