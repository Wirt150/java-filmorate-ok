package ru.yandex.practicum.filmorate.model.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventDto {
    private long timestamp;
    private final long userId;
    private EventType eventType;
    private EventOperation operation;
    private long eventId;
    private final long entityId;
}