package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoStorageUser;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.UserDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
public class DaoUser implements DaoStorageUser {
    private static final String[] USER_COLUMN_NAMES = {"USER_ID"};
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;
    private final FilmRowMapper filmRowMapper;

    @Override
    public UserDto addDto(UserDto userDto) {
        String sql = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) VALUES ( ?,?,?,? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, USER_COLUMN_NAMES);
            stmt.setString(1, userDto.getEmail());
            stmt.setString(2, userDto.getLogin());
            stmt.setString(3, userDto.getName());
            stmt.setDate(4, Date.valueOf(userDto.getBirthday()));
            return stmt;
        }, keyHolder);
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE USER_ID = ?",
                userRowMapper, Objects.requireNonNull(keyHolder.getKey(), "Id нового пользователя равен null").intValue());
    }

    @Override
    public Optional<UserDto> updateDto(UserDto userDto) {
        if (getDtoById(userDto.getId()).isPresent()) {
            jdbcTemplate.update("UPDATE USERS SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?",
                    userDto.getEmail(), userDto.getLogin(), userDto.getName(), userDto.getBirthday(), userDto.getId());
            return Optional.of(userDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> deleteDto(int userId) {
        Optional<UserDto> userDto = getDtoById(userId);
        if (userDto.isPresent()) {
            jdbcTemplate.update("DELETE FROM USERS WHERE user_id = ?", userId);
            return userDto;
        }
        return Optional.empty();
    }

    @Override
    public List<UserDto> getAllDto() {
        return jdbcTemplate.query("SELECT * FROM USERS ORDER BY USER_ID", userRowMapper);
    }

    @Override
    public Optional<UserDto> getDtoById(int id) {
        List<UserDto> query = jdbcTemplate.query(
                "SELECT * FROM USERS WHERE USER_ID = ? ", userRowMapper, id);
        return query.stream().findFirst();
    }

    @Override
    public Optional<UserDto> setMetaValueDto(int dtoId, int metaId) {
        Optional<UserDto> userDto = getDtoById(dtoId);
        if (userDto.isPresent()) {
            jdbcTemplate.update("INSERT INTO FRIENDS (USER_ID, FRIEND_ID) VALUES (?,?)", dtoId, metaId);
            return userDto;
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> deleteMetaValueDto(int dtoId, int metaId) {
        Optional<UserDto> userDto = getDtoById(dtoId);
        if (userDto.isPresent()) {
            jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", dtoId, metaId);
            return userDto;
        }
        return Optional.empty();
    }

    @Override
    public List<UserDto> getMetaValueDto(int count) {
        return jdbcTemplate.query(
                "SELECT U.* FROM FRIENDS F JOIN USERS U on U.USER_ID = F.FRIEND_ID WHERE F.USER_ID = ?",
                userRowMapper, count);
    }

    public List<FilmDto> recommendations(int userId) {
        return jdbcTemplate.query(
                "SELECT F.*, M.MPA_NAME " +
                        "FROM FILMS AS F " +
                        "         INNER JOIN MPA AS M ON F.MPA_ID = M.MPA_ID " +
                        "         INNER JOIN FILMS_LIKE on F.FILM_ID = FILMS_LIKE.FILM_ID " +
                        "         INNER JOIN FILMS_LIKE FL ON FILMS_LIKE.USER_ID = FL.USER_ID " +
                        "         INNER JOIN FILMS_LIKE FLL ON FLL.FILM_ID = FL.FILM_ID AND FLL.USER_ID != FL.USER_ID " +
                        "         JOIN FILMS_LIKE FL1 ON FILMS_LIKE.FILM_ID != FL1.FILM_ID " +
                        "WHERE FLL.USER_ID = ? and FLL.FILM_ID != FILMS_LIKE.FILM_ID " +
                        "GROUP BY FILMS_LIKE.FILM_ID " +
                        "ORDER BY FILMS_LIKE.FILM_ID DESC " +
                        "LIMIT 10", filmRowMapper, userId);
    }
}
