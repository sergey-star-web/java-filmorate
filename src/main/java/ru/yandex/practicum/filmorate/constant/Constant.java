package ru.yandex.practicum.filmorate.constant;

import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final String dateTimeFormatString = "yyyy-MM-dd";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatString);

    public static Genre getGenreById(Long genreId) {
        int counter = 1;
        int genreIdInt = genreId.intValue();
        for (Genre genre : Genre.values()) {
            if (counter == genreIdInt) {
                return genre;
            }
            counter++;
        }
        return null;
    }

    public static MpaRating getMpaRatingById(Integer mpaRatingId) {
        int counter = 1;
        for (MpaRating mpaRating : MpaRating.values()) {
            if (counter == mpaRatingId) {
                return mpaRating;
            }
            counter++;
        }
        return null;
    }
}
