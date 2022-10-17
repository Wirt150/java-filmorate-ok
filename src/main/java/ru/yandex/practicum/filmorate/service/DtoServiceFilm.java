package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dao.DaoStorageFilm;
import ru.yandex.practicum.filmorate.model.FilmDto;

import java.util.List;

public interface DtoServiceFilm extends DaoStorageFilm {

    List<FilmDto> getFilmsByDirectorSortByLikes(int directorId);

    List<FilmDto> getFilmsByDirectorSortByYear(int directorId);

    List<FilmDto> getMostPopular(final int limit, String genreId, final String year);

    List<FilmDto> getCommonFilms(int userId, int friendId);

    List<FilmDto> getSearchFilms(String query, List<String> by);
}
