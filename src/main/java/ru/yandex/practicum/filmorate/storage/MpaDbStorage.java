package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.BaseRepository;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class MpaDbStorage extends BaseRepository<Mpa> implements MpaStorage {
    private final String FIND_ALL_QUERY = "SELECT * FROM mpa_rating";
    private final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_rating WHERE id = ?";

    public MpaDbStorage(JdbcTemplate jdbc, MpaRowMapper mapper) {
        super(jdbc, mapper, Mpa.class);
    }

    public boolean exists(Integer id) {
        Mpa mpa = findOne(FIND_BY_ID_QUERY, id).orElse(null);
        return mpa != null;
    }

    public Mpa getMpa(Integer id) {
        Optional<Mpa> mpaOptional = findOne(FIND_BY_ID_QUERY, id);
        if (mpaOptional.isPresent()) {
            return mpaOptional.get();
        } else {
            throw new NotFoundException("Рейтинг с id " + id + " не найден");
        }
    }

    public List<Mpa> getAllMpa() {
        return findMany(FIND_ALL_QUERY);
    }
}