package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.LikeDbStorage;

import java.util.HashMap;
import java.util.List;

@Service
public class LikeService {
    @Autowired
    private LikeDbStorage likeDbStorage;

    public List<Long> getLikesInFim(Long filmId) {
        return likeDbStorage.getLikesInFim(filmId);
    }

    public HashMap<Long, Long> getAllLikes() {
        return likeDbStorage.getAllLikes();
    }
}