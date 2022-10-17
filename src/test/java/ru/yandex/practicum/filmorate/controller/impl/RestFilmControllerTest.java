package ru.yandex.practicum.filmorate.controller.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerMetaValue;
import ru.yandex.practicum.filmorate.controller.RestControllerExtendedTest;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.MpaDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;
import ru.yandex.practicum.filmorate.service.DtoServiceMetaValue;
import ru.yandex.practicum.filmorate.service.impl.FilmService;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import static ru.yandex.practicum.filmorate.controller.impl.RestUserControllerTest.USER_DTO_TEST;
import static ru.yandex.practicum.filmorate.controller.impl.RestUserControllerTest.USER_DTO_UPDATE_TEST;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestFilmControllerTest extends RestControllerExtendedTest<FilmDto> {

    @Autowired
    private final DtoServiceRead<FilmDto> dtoServiceRead;
    @Autowired
    private final DtoServiceCrud<FilmDto> dtoServiceCrud;
    @Autowired
    private final DtoServiceMetaValue<FilmDto> dtoServiceMetaValue;
    @Autowired
    private final FilmService filmService;

    @Autowired
    private final RestUserController userController;

    private final FilmDto filmDtoTest = FilmDto.builder()
            .id(0)
            .name("filmDtoTest")
            .description("Description")
            .releaseDate(LocalDate.of(2000, Month.JANUARY, 1))
            .duration(200)
            .genres(new ArrayList<>())
            .mpa(new MpaDto(1, "G"))
            .build();

    private final FilmDto filmDtoUpdateTest = FilmDto.builder()
            .id(1)
            .name("filmDtoUpdateTest")
            .description("Description")
            .releaseDate(LocalDate.of(2000, Month.JANUARY, 1))
            .duration(200)
            .genres(new ArrayList<>())
            .mpa(new MpaDto(1, "G"))
            .build();

    @Override
    protected RestAbstractControllerMetaValue<FilmDto> createRestController() {
        return new RestFilmController(dtoServiceRead, dtoServiceCrud, dtoServiceMetaValue, filmService);
    }

    @Override
    protected FilmDto createDto() {
        return filmDtoTest;
    }

    @Override
    protected FilmDto createDtoTest() {
        return filmDtoUpdateTest;
    }
    @Test
    @Override
    @DisplayName("Checking the POST Method by controllers.")
    protected void whenCheckCreateMethod() {
        userController.create(USER_DTO_TEST, httpServletRequest);
        super.whenCheckCreateMethod();
    }
    @Test
    @Override
    @DisplayName("Checking the PUT Method by controllers.")
    protected void whenCheckUpdateMethod() {
        userController.create(USER_DTO_TEST, httpServletRequest);
        super.whenCheckUpdateMethod();
    }
    @Test
    @Override
    @DisplayName("Checking the GET(id) Method by controllers.")
    protected void whenCheckGetIdMethod() {
        userController.create(USER_DTO_TEST, httpServletRequest);
        super.whenCheckGetIdMethod();
    }

    @Test
    @Override
    @DisplayName("Checking the PUT(setMetaValue) Method by controllers.")
    protected void whenCheckPutMetaValueMethod() {
        userController.create(USER_DTO_TEST, httpServletRequest);
        userController.create(USER_DTO_UPDATE_TEST, httpServletRequest);
        super.whenCheckPutMetaValueMethod();
    }

    @Test
    @Override
    @DisplayName("Checking the DELETE(deleteMetaValue) Method by controllers.")
    protected void whenCheckDeleteMetaValueMethod() {
        userController.create(USER_DTO_TEST, httpServletRequest);
        userController.create(USER_DTO_UPDATE_TEST, httpServletRequest);
        super.whenCheckDeleteMetaValueMethod();
    }

    @Test
    @Override
    @DisplayName("Checking the GET(getMetaValueDto) Method by controllers.")
    protected void whenCheckGetMetaValueMethod() {
        userController.create(USER_DTO_TEST, httpServletRequest);
        userController.create(USER_DTO_UPDATE_TEST, httpServletRequest);
        super.whenCheckGetMetaValueMethod();
    }
}
