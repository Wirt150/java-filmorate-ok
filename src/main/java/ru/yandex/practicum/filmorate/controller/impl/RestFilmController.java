package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerMetaValue;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceFilm;
import ru.yandex.practicum.filmorate.service.DtoServiceMetaValue;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/films")
public class RestFilmController extends RestAbstractControllerMetaValue<FilmDto> {

    private final DtoServiceFilm serviceFilm;

    protected RestFilmController(
            DtoServiceRead<FilmDto> dtoServiceRead,
            DtoServiceCrud<FilmDto> dtoServiceCrud,
            DtoServiceMetaValue<FilmDto> dtoServiceMetaValue,
            DtoServiceFilm filmService) {
        super(dtoServiceCrud, dtoServiceRead, dtoServiceMetaValue);
        this.serviceFilm = filmService;
    }

    @Override
    @PutMapping("{id}/like/{userId}")
    public Optional<FilmDto> setMetaValue(
            @PathVariable("id") final int dtoId,
            @PathVariable("userId") final int metaId,
            HttpServletRequest request) {
        return super.setMetaValue(dtoId, metaId, request);
    }

    @Override
    @DeleteMapping("{id}/like/{filmId}")
    public Optional<FilmDto> deleteMetaValue(
            @PathVariable("id") final int dtoId,
            @PathVariable("filmId") final int metaId,
            HttpServletRequest request) {
        return super.deleteMetaValue(dtoId, metaId, request);
    }

    @GetMapping("popular")
    public List<FilmDto> getMetaValueDto(
            @RequestParam(value = "count", defaultValue = "10") final int limit,
            @RequestParam(required = false) final String genreId,
            @RequestParam(required = false) final String year,
            HttpServletRequest request
    ) {
        logging(request);
        if (genreId == null && year == null) {
            return dtoServiceMetaValue.getMetaValueDto(limit);
        }
        return serviceFilm.getMostPopular(limit, genreId, year);
    }

    @GetMapping("common")
    public List<FilmDto> getCommonFilms(
            @RequestParam final int userId,
            @RequestParam final int friendId,
            HttpServletRequest request
    ) {
        logging(request);
        return serviceFilm.getCommonFilms(userId, friendId);
    }

    @GetMapping("search")
    public List<FilmDto> getSearch(
            @RequestParam final String query,
            @RequestParam final List<String> by,
            HttpServletRequest request
    ) {
        logging(request);
        return serviceFilm.getSearchFilms(query, by);
    }

    @GetMapping("/director/{directorId}")
    public List<FilmDto> getFilmsByDirectorSort(
            @PathVariable final int directorId,
            @RequestParam String sortBy,
            HttpServletRequest request
    ) {
        logging(request);
        if (Objects.equals("likes",sortBy)) {
            return serviceFilm.getFilmsByDirectorSortByLikes(directorId);
        } else if (Objects.equals("year",sortBy)) {
            return serviceFilm.getFilmsByDirectorSortByYear(directorId);
        }
        return new ArrayList<>();
    }
}