package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerRead;
import ru.yandex.practicum.filmorate.model.MpaDto;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

@RestController
@RequestMapping("/mpa")
public class RestMpaController extends RestAbstractControllerRead<MpaDto> {
    @Autowired
    public RestMpaController(DtoServiceRead<MpaDto> dtoService) {
        super(dtoService);
    }
}
