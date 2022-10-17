package ru.yandex.practicum.filmorate.dao;

import java.util.List;
import java.util.Optional;

public interface DaoStorageMetaValue<T> extends DaoStorageCrud<T> {

    Optional<T> setMetaValueDto(final int dtoId, final int metaId);

    Optional<T> deleteMetaValueDto(final int dtoId, final int metaId);

    List<T> getMetaValueDto(final int count);
}
