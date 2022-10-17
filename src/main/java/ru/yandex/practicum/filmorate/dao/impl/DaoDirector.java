package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoStorageDirector;
import ru.yandex.practicum.filmorate.mapper.DirectorRowMapper;
import ru.yandex.practicum.filmorate.model.DirectorDto;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DaoDirector implements DaoStorageDirector {

    private final JdbcTemplate jdbcTemplate;
    private final DirectorRowMapper directorRowMapper;

    @Override
    public DirectorDto addDto(DirectorDto directorDto) {
        jdbcTemplate.update("INSERT INTO directors ( director_name) VALUES (?)", directorDto.getName());
        return makeDirector(directorDto);
    }

    @Override
    public Optional<DirectorDto> updateDto(DirectorDto directorDto) {
        if (getDtoById(directorDto.getId()).isPresent()) {
            jdbcTemplate.update("UPDATE directors SET director_name = ? WHERE director_id = ?",
                    directorDto.getName(), directorDto.getId());
            return Optional.of(makeDirector(directorDto));
        }
        return Optional.empty();
    }

    @Override
    public Optional<DirectorDto> deleteDto(int directorId) {
        Optional<DirectorDto> directorDto = getDtoById(directorId);
        if (directorDto.isPresent()) {
            jdbcTemplate.update("DELETE FROM directors WHERE director_id = ?", directorId);
            return directorDto;
        }
        return Optional.empty();
    }

    @Override
    public Optional<DirectorDto> getDtoById(int directorId) {
        List<DirectorDto> directors = jdbcTemplate.query(
                "SELECT * FROM directors WHERE director_id = ?", directorRowMapper, directorId);
        return directors.stream().findFirst();
    }

    @Override
    public List<DirectorDto> getAllDto() {
        return jdbcTemplate.query("SELECT * FROM directors", directorRowMapper);
    }

    private DirectorDto makeDirector(DirectorDto directorDto) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM directors WHERE director_name = ?", directorRowMapper, directorDto.getName());
    }
}
