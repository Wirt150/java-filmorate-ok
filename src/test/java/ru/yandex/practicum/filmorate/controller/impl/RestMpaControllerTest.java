package ru.yandex.practicum.filmorate.controller.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerRead;
import ru.yandex.practicum.filmorate.controller.RestControllerTest;
import ru.yandex.practicum.filmorate.model.MpaDto;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestMpaControllerTest extends RestControllerTest<MpaDto> {
    @Autowired
    private final DtoServiceRead<MpaDto> service;

    private final MpaDto testDto = new MpaDto(1, "G");

    @Override
    protected RestAbstractControllerRead<MpaDto> createRestController() {
        return new RestMpaController(service);
    }

    @Override
    protected MpaDto createDto() {
        return testDto;
    }
}
