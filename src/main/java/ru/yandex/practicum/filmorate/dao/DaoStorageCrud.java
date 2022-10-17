package ru.yandex.practicum.filmorate.dao;

import java.util.Optional;

public interface DaoStorageCrud<T> extends DaoStorageRead<T> {

    T addDto(final T t);

    Optional<T> updateDto(final T t);

    Optional<T> deleteDto(final int id);

}
