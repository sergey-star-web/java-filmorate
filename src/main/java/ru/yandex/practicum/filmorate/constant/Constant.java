package ru.yandex.practicum.filmorate.constant;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final String dateTimeFormatString = "yyyy-MM-dd";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatString);
}
