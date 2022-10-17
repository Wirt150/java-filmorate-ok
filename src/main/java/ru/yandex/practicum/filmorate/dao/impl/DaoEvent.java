package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoStorageEvent;
import ru.yandex.practicum.filmorate.mapper.EventRowMapper;
import ru.yandex.practicum.filmorate.model.event.EventDto;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@Primary
@AllArgsConstructor
public class DaoEvent implements DaoStorageEvent {

    private static final String[] EVENT_COLUMN_NAMES = {"EVENT_ID"};
    private JdbcTemplate jdbcTemplate;
    private EventRowMapper eventRowMapper;

    @Override
    public void add(EventDto event) {
        String sqlQuery = "INSERT INTO EVENT (USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID) " +
                "VALUES (?, ?, ?, ?) ";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sqlQuery, EVENT_COLUMN_NAMES);
            stmt.setLong(1, event.getUserId());
            stmt.setString(2, String.valueOf(event.getEventType()));
            stmt.setString(3, String.valueOf(event.getOperation()));
            stmt.setLong(4, event.getEntityId());
            return stmt;
        }, keyHolder);
    }

    @Override
    public List<EventDto> getUserEvents(long userId) {
        return jdbcTemplate.query(
                "SELECT EVENT_ID, TIMESTAMP_EVENT, USER_ID, EVENT_TYPE, OPERATION, ENTITY_ID " +
                "FROM EVENT " +
                "WHERE USER_ID = ?" +
                "ORDER BY TIMESTAMP_EVENT", eventRowMapper, userId);
    }
}