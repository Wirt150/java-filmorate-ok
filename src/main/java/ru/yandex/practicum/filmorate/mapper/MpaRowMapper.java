package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaDto;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class MpaRowMapper implements RowMapper<MpaDto> {
    @Override
    public MpaDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return MpaDto.builder()
                .id(rs.getInt("MPA_ID"))
                .name(rs.getString("MPA_NAME"))
                .build();
    }
}