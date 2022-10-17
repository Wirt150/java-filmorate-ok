package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.validator.ValidUtils;

import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmDtoTest {

    @Test
    @DisplayName("Creates a valid dto object, and checks its validity.")
    void whenCreateValidFilmDtoThenCreated() {

        FilmDto filmDtoTest = FilmDto.builder()
                .id(0)
                .name("Name")
                .description("Description")
                .releaseDate(LocalDate.of(2000, Month.JANUARY, 1))
                .duration(200)
                .genres(new ArrayList<>())
                .mpa(new MpaDto(1,"G"))
                .build();

        //test
        assertEquals(0, ValidUtils.testingValidator(filmDtoTest).size(), "Список должен быть пустой.");
    }

    @Test
    @DisplayName("Creates a not valid dto object, and checks its validity.")
    void whenNotCreateValidFilmDtoThenCreated() {

        FilmDto filmDtoTest = FilmDto.builder()
                .id(0)
                .name("")
                .description("Description".repeat(20))
                .releaseDate(LocalDate.of(1895, Month.DECEMBER, 27))
                .duration(-1)
                .genres(new ArrayList<>())
                .mpa(null)
                .build();

        Set<ConstraintViolation<FilmDto>> errors = ValidUtils.testingValidator(filmDtoTest);

        //test
        assertEquals(5,errors.size(), "Размер списка должен быть равен 5.");
    }
}
