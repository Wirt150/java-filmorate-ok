package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerRead;
import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

@RestController
@RequestMapping("/genres")
public class RestGenreController extends RestAbstractControllerRead<GenreDto> {
    @Autowired
    public RestGenreController(DtoServiceRead<GenreDto> dtoService) {
        super(dtoService);
    }
}
