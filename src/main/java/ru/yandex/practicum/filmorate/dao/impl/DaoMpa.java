package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoStorageRead;
import ru.yandex.practicum.filmorate.mapper.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.MpaDto;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DaoMpa implements DaoStorageRead<MpaDto> {

    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaRowMapper;

    @Override
    public Optional<MpaDto> getDtoById(int id) {
        List<MpaDto> query = jdbcTemplate.query(
                "SELECT * FROM MPA WHERE MPA_ID = ? ", mpaRowMapper, id);
        return query.stream().findFirst();
    }

    @Override
    public List<MpaDto> getAllDto() {
        return jdbcTemplate.query("SELECT * FROM MPA ORDER BY MPA_ID", mpaRowMapper);
    }

}


