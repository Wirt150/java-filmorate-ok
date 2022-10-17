package ru.yandex.practicum.filmorate.model.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private String errors;
    private String message;
}
