package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmDto;

import java.util.List;

public interface DaoStorageFilm extends DaoStorageMetaValue<FilmDto> {
    List<FilmDto> getFilmsByDirectorSortByLikes(int directorId);

    List<FilmDto> getFilmsByDirectorSortByYear(int directorId);

    List<FilmDto> getMostPopular(final int limit, final String byGenre, final String byYear);

    List<FilmDto> getCommonFilms(int userId, int friendId);

    default List<FilmDto> getSearchFilms(String where, String whereOr) {
        return List.of();
    }
}