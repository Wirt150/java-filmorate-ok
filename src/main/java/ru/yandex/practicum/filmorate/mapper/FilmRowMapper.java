package ru.yandex.practicum.filmorate.mapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.DirectorDto;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.model.MpaDto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FilmRowMapper implements RowMapper<FilmDto> {
    private final JdbcTemplate jdbcTemplate;

    public FilmRowMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private List<GenreDto> makeGenres(int id) {
        return jdbcTemplate.query(
                "SELECT G.* FROM GENRES_FILM AS GF JOIN GENRES AS G ON G.GENRE_ID = GF.GENRE_ID WHERE GF.FILM_ID = ?",
                new GenreRowMapper(), id);
    }

    private List<DirectorDto> makeDirectors(int id) {
        return jdbcTemplate.query(
                "SELECT D.* FROM directors_film AS df JOIN directors AS d ON d.director_id = df.director_id " +
                        "WHERE df.FILM_ID = ?",
                new DirectorRowMapper(), id);
    }

    @Override
    public FilmDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmDto.builder()
                .id(rs.getInt("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .rate(rs.getInt("RATE"))
                .mpa(MpaDto.builder().id(rs.getInt("MPA_ID")).name(rs.getString("MPA_NAME")).build())
                .genres(makeGenres(rs.getInt("FILM_ID")))
                .directors(makeDirectors(rs.getInt("FILM_ID")))
                .build();
    }
}