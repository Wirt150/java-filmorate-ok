package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import java.util.Optional;

public interface DaoStorageRead<T> {
    Optional<T> getDtoById(final int id);

    List<T> getAllDto();
}
