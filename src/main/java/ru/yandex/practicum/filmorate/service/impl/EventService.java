package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DaoStorageEvent;
import ru.yandex.practicum.filmorate.model.event.EventDto;
import ru.yandex.practicum.filmorate.model.event.EventOperation;
import ru.yandex.practicum.filmorate.model.event.EventType;
import ru.yandex.practicum.filmorate.service.DtoServiceEvent;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService implements DtoServiceEvent {

    private final DaoStorageEvent eventStorage;

    @Override
    public List<EventDto> getUserEvents(long userId) {
        return eventStorage.getUserEvents(userId);
    }

    @Override
    public void removeLikeEvent(long userId, long entityId) {
        EventDto event = EventDto.builder().userId(userId).entityId(entityId).build();
        event.setEventType(EventType.LIKE);
        event.setOperation(EventOperation.REMOVE);
        eventStorage.add(event);
    }

    @Override
    public void addLikeEvent(long userId, long entityId) {
        EventDto event = EventDto.builder().userId(userId).entityId(entityId).build();
        event.setEventType(EventType.LIKE);
        event.setOperation(EventOperation.ADD);
        eventStorage.add(event);
    }

    @Override
    public void removeFriendEvent(long userId, long entityId) {
        EventDto event = EventDto.builder().userId(userId).entityId(entityId).build();
        event.setEventType(EventType.FRIEND);
        event.setOperation(EventOperation.REMOVE);
        eventStorage.add(event);
    }

    @Override
    public void addFriendEvent(long userId, long entityId) {
        EventDto event = EventDto.builder().userId(userId).entityId(entityId).build();
        event.setEventType(EventType.FRIEND);
        event.setOperation(EventOperation.ADD);
        eventStorage.add(event);
    }

    @Override
    public void removeReviewEvent(long userId, long entityId) {
        EventDto event = EventDto.builder().userId(userId).entityId(entityId).build();
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.REMOVE);
        eventStorage.add(event);
    }

    @Override
    public void addReviewEvent(long userId, long entityId) {
        EventDto event = EventDto.builder().userId(userId).entityId(entityId).build();
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.ADD);
        eventStorage.add(event);
    }

    @Override
    public void updateReviewEvent(long userId, long entityId) {
        EventDto event = EventDto.builder().userId(userId).entityId(entityId).build();
        event.setEventType(EventType.REVIEW);
        event.setOperation(EventOperation.UPDATE);
        eventStorage.add(event);
    }
}