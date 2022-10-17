package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.event.EventDto;

import java.util.List;

public interface DtoServiceEvent {

    List<EventDto> getUserEvents(long userId);

    void removeLikeEvent(long userId, long entityId);

    void addLikeEvent(long userId, long entityId);

    void removeFriendEvent(long userId, long entityId);

    void addFriendEvent(long userId, long entityId);

    void removeReviewEvent(long userId, long entityId);

    void addReviewEvent(long userId, long entityId);

    void updateReviewEvent(long userId, long entityId);
}
