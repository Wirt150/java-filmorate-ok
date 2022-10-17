package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.dao.DaoStorageUser;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceMetaValue;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;
import ru.yandex.practicum.filmorate.service.DtoServiceUsers;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements DtoServiceUsers, DtoServiceRead<UserDto>, DtoServiceCrud<UserDto>, DtoServiceMetaValue<UserDto> {

    private final DaoStorageUser daoStorage;
    private final EventService eventService;

    @Override
    public UserDto addDto(UserDto userDto) {
        setInitialValue(userDto);
        return daoStorage.addDto(userDto);
    }

    @Override
    public Optional<UserDto> updateDto(UserDto userDto) {
        setInitialValue(userDto);
        daoStorage.updateDto(userDto)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь c id: " + userDto.getId() + " не найден."));
        return Optional.of(userDto);
    }

    @Override
    public Optional<UserDto> deleteDto(int userId) {
        return Optional.of(daoStorage.deleteDto(userId)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с id " + userId + " не найден.")));
    }

    @Override
    public Optional<UserDto> getDtoById(int id) {
        return Optional.of(daoStorage.getDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь с id " + id + " не найден.")));
    }

    @Override
    public List<UserDto> getAllDto() {
        return daoStorage.getAllDto();
    }

    @Override
    public Optional<UserDto> setMetaValueDto(final int dtoId, final int metaId) {
        Optional<UserDto> userDto = daoStorage.setMetaValueDto(dtoId, metaId);
        if(userDto.isPresent()) {
            eventService.addFriendEvent(dtoId, metaId);
            return userDto;
        }
        throw new IllegalArgumentException("Пользователь с id " + dtoId + " не найден.");
    }

    @Override
    public Optional<UserDto> deleteMetaValueDto(final int dtoId, final int metaId) {
        Optional<UserDto> userDto = daoStorage.deleteMetaValueDto(dtoId, metaId);
        if(userDto.isPresent()) {
            eventService.removeFriendEvent(dtoId, metaId);
            return userDto;
        }
        throw new IllegalArgumentException("Пользователь с id " + dtoId + " не найден.");
    }

    @Override
    public List<UserDto> getMetaValueDto(final int id) {
        getDtoById(id);
        return daoStorage.getMetaValueDto(id);
    }

    private void setInitialValue(UserDto userDto) {
        if (!StringUtils.hasText(userDto.getName())) {
            userDto.setName(userDto.getLogin());
        }
    }

    public List<FilmDto> recommendations(int userId) {
        getDtoById(userId);
        return daoStorage.recommendations(userId);
    }
}
