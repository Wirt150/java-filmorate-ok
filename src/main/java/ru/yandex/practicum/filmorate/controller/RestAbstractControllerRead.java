package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Validated
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public abstract class RestAbstractControllerRead<T> {
    protected static final String LOG_INFO = "Получен запрос к эндпоинту: {}, с методом {}. ";
    protected final DtoServiceRead<T> dtoServiceRead;

    @GetMapping
    public List<T> findAll(HttpServletRequest request) {
        log.info(LOG_INFO + "На получение общего списка.", request.getRequestURI(), request.getMethod());
        return dtoServiceRead.getAllDto();
    }

    @GetMapping("{id}")
    public Optional<T> findById(@PathVariable int id, HttpServletRequest request) {
        log.info(LOG_INFO, request.getRequestURI(), request.getMethod());
        return dtoServiceRead.getDtoById(id);
    }

    protected void logging(HttpServletRequest request) {
        log.info(LOG_INFO, request.getRequestURI(), request.getMethod());
    }
}
