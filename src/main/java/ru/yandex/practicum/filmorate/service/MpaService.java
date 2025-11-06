package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

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

    public boolean exists(Integer id) {
        return mpaStorage.exists(id);
    }
}