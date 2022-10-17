package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DaoStorageRead;
import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService implements DtoServiceRead<GenreDto> {

    private final DaoStorageRead<GenreDto> daoStorage;

    @Override
    public Optional<GenreDto> getDtoById(int id) {
        GenreDto genreDto = daoStorage.getDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Жанра с идентификатором " + id + " не найдено."));
        return Optional.of(genreDto);
    }

    @Override
    public List<GenreDto> getAllDto() {
        return daoStorage.getAllDto();
    }

}
