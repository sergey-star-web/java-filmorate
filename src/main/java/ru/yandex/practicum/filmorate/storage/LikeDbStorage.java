package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class LikeDbStorage implements LikeStorage {
    @Autowired
    private JdbcTemplate jdbc;

    public LikeDbStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Long> getLikesInFim(Long filmId) {
        String findByIdLikesQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        return jdbc.query(findByIdLikesQuery, (rs, rn) -> rs.getLong(1), filmId);
    }

    public HashMap<Long, Long> getAllLikes() {
        String findByIdLikesQuery = "SELECT user_id, film_id FROM likes";
        HashMap<Long, Long> likesMap = new HashMap<>();
        jdbc.query(findByIdLikesQuery, (rs, rn) -> {
            Long userId = rs.getLong("user_id");
            Long filmId = rs.getLong("film_id");
            likesMap.put(userId, filmId);
            return likesMap; // Возврат карты для каждого ряда не требуется, но нужен для соответствия сигнатуре метода
        });
        return likesMap;
    }
}