package ru.yandex.practicum.filmorate.constant;

import java.time.format.DateTimeFormatter;

import static java.time.LocalDate.now;

public class Constant {
    public static final String dateTimeFormatString = "yyyy-MM-dd";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormatString);
    public static final String nowString = now().format(formatter);
}
