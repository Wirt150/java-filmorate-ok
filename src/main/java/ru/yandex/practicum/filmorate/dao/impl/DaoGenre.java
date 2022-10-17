package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoStorageRead;
import ru.yandex.practicum.filmorate.mapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.GenreDto;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DaoGenre implements DaoStorageRead<GenreDto> {

    private final JdbcTemplate jdbcTemplate;
    public final GenreRowMapper genreRowMapper;

    @Override
    public Optional<GenreDto> getDtoById(int id) {
        List<GenreDto> query = jdbcTemplate.query(
                "SELECT * FROM GENRES WHERE GENRE_ID = ? ", genreRowMapper, id);
        return query.stream().findFirst();
    }

    @Override
    public List<GenreDto> getAllDto() {
        return jdbcTemplate.query("SELECT * FROM GENRES ORDER BY GENRE_ID", genreRowMapper);
    }
}
