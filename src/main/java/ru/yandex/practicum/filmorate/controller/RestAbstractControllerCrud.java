package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.DirectorDto;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Component
@Validated
public abstract class RestAbstractControllerCrud<T> extends RestAbstractControllerRead<T> {
    protected static final String LOG_INFO = "Получен запрос к эндпоинту: {}, с методом {}. ";
    protected final DtoServiceCrud<T> dtoServiceCrud;

    protected RestAbstractControllerCrud(DtoServiceCrud<T> dtoServiceCrud, DtoServiceRead<T> dtoServiceRead) {
        super(dtoServiceRead);
        this.dtoServiceCrud = dtoServiceCrud;
    }

    @PostMapping
    public T create(@Valid @RequestBody final T dto, HttpServletRequest request) {
        log.info(LOG_INFO + "Dto: {}.", request.getRequestURI(), request.getMethod(), dto);
        return dtoServiceCrud.addDto(dto);
    }

    @PutMapping
    public Optional<T> update(@Valid @RequestBody final T dto, HttpServletRequest request) {
        log.info(LOG_INFO + "Dto: {}.", request.getRequestURI(), request.getMethod(), dto);
        return dtoServiceCrud.updateDto(dto);
    }

    @DeleteMapping("{id}")
    public Optional<T> delete(@PathVariable final int id, HttpServletRequest request) {
        log.info(LOG_INFO + "Dto: {}.", request.getRequestURI(), request.getMethod(), id);
        return dtoServiceCrud.deleteDto(id);
    }
}
