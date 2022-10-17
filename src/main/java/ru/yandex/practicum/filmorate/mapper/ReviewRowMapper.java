package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.ReviewDto;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class ReviewRowMapper implements RowMapper<ReviewDto> {
    @Override
    public ReviewDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return ReviewDto.builder()
                .reviewId(rs.getInt("REVIEW_ID"))
                .content(rs.getString("CONTENT"))
                .isPositive(rs.getBoolean("IS_POSITIVE"))
                .userId(rs.getInt("USER_ID"))
                .filmId(rs.getInt("FILM_ID"))
                .useful(rs.getInt("USEFUL"))
                .build();
    }
}
