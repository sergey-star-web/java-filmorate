package ru.yandex.practicum.filmorate.storage;

import java.util.HashMap;
import java.util.List;

public interface LikeStorage {
    List<Long> getLikesInFim(Long filmId);

    HashMap<Long, Long> getAllLikes();
}