package ru.yandex.practicum.filmorate.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerRead;
import ru.yandex.practicum.filmorate.controller.RestControllerTest;
import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestGenreControllerTest extends RestControllerTest<GenreDto> {

    @Autowired
    private final DtoServiceRead<GenreDto> service;

    private final GenreDto testDto = new GenreDto(1, "Комедия");

    @Override
    protected RestAbstractControllerRead<GenreDto> createRestController() {
        return new RestGenreController(service);
    }

    @Override
    protected GenreDto createDto() {
        return testDto;
    }
}
