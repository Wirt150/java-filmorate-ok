package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.UserDto;

import java.util.List;

public interface DaoStorageUser extends DaoStorageMetaValue<UserDto> {
    List<FilmDto> recommendations(int userId);
}
