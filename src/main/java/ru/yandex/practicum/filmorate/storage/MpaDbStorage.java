package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Optional;

@Slf4j
@Repository
public class MpaDbStorage extends BaseRepository<Mpa> {
    public MpaDbStorage(JdbcTemplate jdbc, MpaRowMapper mapper) {
        super(jdbc, mapper, Mpa.class);
    }
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_rating WHERE id = ?";

    public boolean exists(Integer id) {
        Mpa mpa = findOne(FIND_BY_ID_QUERY, id).orElse(null);
        return mpa != null;
    }

    public Mpa getMpa(Integer id) {
        Optional<Mpa> mpaOptional = findOne(FIND_BY_ID_QUERY, id);
        return mpaOptional.orElse(null);
    }
}
