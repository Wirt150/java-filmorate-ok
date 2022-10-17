package ru.yandex.practicum.filmorate.validator;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.Month;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateDate.DateValidator.class)
@Documented
public @interface ValidateDate {

    String message() default "{ru.yandex.practicum.filmorate.validaror.ValidateDate.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class DateValidator implements ConstraintValidator<ValidateDate, LocalDate> {
        private static final LocalDate MIN_DATE_FILM = LocalDate.of(1895, Month.DECEMBER, 28);

        @Override
        public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
            return !value.isBefore(MIN_DATE_FILM);
        }
    }
}
