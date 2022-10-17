package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerCrud;
import ru.yandex.practicum.filmorate.model.DirectorDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

@RestController
@RequestMapping("/directors")
public class RestDirectorController extends RestAbstractControllerCrud<DirectorDto> {

    protected RestDirectorController(
            DtoServiceCrud<DirectorDto> dtoServiceCrud,
            DtoServiceRead<DirectorDto> dtoServiceRead
            ) {
        super(dtoServiceCrud, dtoServiceRead);
    }
}
