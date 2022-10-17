package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.MpaDto;
import ru.yandex.practicum.filmorate.model.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.exception.ValidationErrorResponse;
import ru.yandex.practicum.filmorate.model.exception.Violation;
import ru.yandex.practicum.filmorate.validator.ValidUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class RestExceptionControllerTest {
    private final RestExceptionController errorHandler;

    public RestExceptionControllerTest() {
        this.errorHandler = new RestExceptionController();
    }

    @Test
    @DisplayName("Checking the ErrorResponse by ErrorHandler.")
    void whenCreateExceptionAndTestErrorResponse() {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("ErrorTest");
        Throwable throwable = new Throwable("ErrorTest");

        ErrorResponse errorResponseIllegalArgumentException = errorHandler.errorResponse(illegalArgumentException);
        ErrorResponse errorResponseThrowable = errorHandler.handleThrowable(throwable);

        //test
        assertEquals(
                "ErrorTest", errorResponseIllegalArgumentException.getMessage(), "Сообщения должны совпадать.");
        assertEquals(
                "ErrorTest", errorResponseThrowable.getMessage(), "Сообщения должны совпадать.");
    }

    @Test
    @DisplayName("Checking the ValidationErrorResponse by ErrorHandler.")
    void whenCreateExceptionAndTestValidationErrorResponse() {
        FilmDto filmDtoTest = FilmDto.builder()
                .id(0)
                .name("")
                .description("Description")
                .releaseDate(LocalDate.of(2000, Month.JANUARY, 1))
                .duration(200)
                .genres(new ArrayList<>())
                .mpa(new MpaDto(1,"G"))
                .build();

        Set<ConstraintViolation<FilmDto>> errors = ValidUtils.testingValidator(filmDtoTest);
        ConstraintViolationException constraintViolationException = new ConstraintViolationException(errors);
        ValidationErrorResponse validationErrorResponse = errorHandler.onConstraintValidationException(constraintViolationException);

        Violation violation = validationErrorResponse.getViolations().get(0);
        System.out.println(violation.toString());

        //test
        assertEquals(1, validationErrorResponse.getViolations().size(), "Размер списка должен быть равен 1.");
        assertEquals("name", violation.getFieldName(), "Название поля должно совпадать");
        assertEquals("Название фильма не может быть пустым.", violation.getMessage(), "Описание ошибки должно совпадать");
    }
}
