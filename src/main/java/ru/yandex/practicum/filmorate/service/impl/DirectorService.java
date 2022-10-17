package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DaoStorageDirector;
import ru.yandex.practicum.filmorate.model.DirectorDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceDirector;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DirectorService implements DtoServiceDirector, DtoServiceCrud<DirectorDto>, DtoServiceRead<DirectorDto> {

    private final DaoStorageDirector daoStorage;

    @Override
    public List<DirectorDto> getAllDto() {
        return daoStorage.getAllDto();
    }

    @Override
    public Optional<DirectorDto> getDtoById(int id) {
        return Optional.of(daoStorage.getDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Режиссер с идентификатором " + id + " не найден.")));
    }

    @Override
    public DirectorDto addDto(DirectorDto directorDto) {
        return daoStorage.addDto(directorDto);
    }

    @Override
    public Optional<DirectorDto> updateDto(DirectorDto directorDto) {
        return Optional.of(daoStorage.updateDto(directorDto)
                .orElseThrow(() -> new IllegalArgumentException("Режиссер c id: " + directorDto.getId() + " не найден.")));
    }

    @Override
    public Optional<DirectorDto> deleteDto(int id) {
        return Optional.of(daoStorage.deleteDto(id)
                .orElseThrow(() -> new IllegalArgumentException("Режиссер c id: " + id + " не найден.")));
    }
}
