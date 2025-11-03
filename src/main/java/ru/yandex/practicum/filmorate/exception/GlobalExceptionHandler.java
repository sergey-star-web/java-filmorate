package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final String error = "error";
    private final String message = "message";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, Object>> notFoundHandler(NotFoundException e) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("type", "about:blank");
        responseBody.put("title", "Not Found");
        responseBody.put("status", 404);
        responseBody.put("detail", e.getMessage());
        responseBody.put("instance", "/films");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler(ValidationException e) {
        return Map.of(error, "Ошибка валидации.",
                message, e.getMessage());
    }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
   public Map<String, String> friendsAddingExceptionHandler(FriendsAddException e) {
       return Map.of(error, "Ошибка добавления друга",
               message, e.getMessage());
   }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> likesSendingExceptionHandler(LikeAddException e) {
        return Map.of(error, "Ошибка добавления лайка",
                message, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(Exception ex) {
        return Map.of(error, "Ошибка добавления лайка",
                message, ex.getMessage());
    }
}