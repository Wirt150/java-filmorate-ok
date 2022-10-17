package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DaoStorageRead;
import ru.yandex.practicum.filmorate.model.MpaDto;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MpaService implements DtoServiceRead<MpaDto> {

    private final DaoStorageRead<MpaDto> daoStorage;

    @Override
    public Optional<MpaDto> getDtoById(int id) {
        MpaDto mpaDto = daoStorage.getDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("MPA с идентификатором " + id + " не найден."));
        return Optional.of(mpaDto);
    }

    @Override
    public List<MpaDto> getAllDto() {
        return daoStorage.getAllDto();
    }

}
