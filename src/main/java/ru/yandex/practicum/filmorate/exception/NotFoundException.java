package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException() {
        super();
    }
}