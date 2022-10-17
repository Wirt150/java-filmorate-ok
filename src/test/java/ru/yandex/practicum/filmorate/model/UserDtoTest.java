package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDtoTest {

    @Test
    @DisplayName("Creates a valid dto object, and checks its validity.")
    void whenCreateValidUserDtoThenCreated() {

        UserDto userDtoTest = UserDto.builder()
                .id(0)
                .email("test@test.test")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2000, Month.JANUARY, 1))
                .build();

        //test
        assertEquals(0, testingValidator(userDtoTest).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Creates a not valid dto object, and checks its validity.")
    void whenNotCreateValidUserDtoThenCreated() {

        UserDto userDtoTest = UserDto.builder()
                .id(0)
                .email("")
                .login(" ")
                .name("Name")
                .birthday(LocalDate.now())
                .build();

        Set<ConstraintViolation<UserDto>> errors = testingValidator(userDtoTest);
        errors.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);

        //test
        assertEquals(5, errors.size(), "Размер списка должен быть равен 5.");
    }

    private Set<ConstraintViolation<UserDto>> testingValidator(UserDto userDto) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(userDto);
    }

}
