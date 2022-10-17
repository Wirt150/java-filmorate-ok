package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.event.EventDto;

import java.util.List;

public interface DaoStorageEvent {

    void add(EventDto event);

    List<EventDto> getUserEvents(long userId);
}
