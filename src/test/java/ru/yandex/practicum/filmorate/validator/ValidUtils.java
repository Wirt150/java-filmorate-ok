package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.model.FilmDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidUtils {

    public static Set<ConstraintViolation<FilmDto>> testingValidator(FilmDto filmDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(filmDto);
    }
}
