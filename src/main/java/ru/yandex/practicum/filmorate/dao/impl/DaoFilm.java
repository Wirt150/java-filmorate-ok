package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoStorageFilm;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.FilmDto;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Primary
@Repository
@RequiredArgsConstructor
public class DaoFilm implements DaoStorageFilm {

    private static final String[] FILM_COLUMN_MANES = {"FILM_ID"};
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Override
    public FilmDto addDto(FilmDto filmDto) {
        String sql = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATE, MPA_ID) VALUES (?,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, FILM_COLUMN_MANES);
            stmt.setString(1, filmDto.getName());
            stmt.setString(2, filmDto.getDescription());
            stmt.setDate(3, Date.valueOf(filmDto.getReleaseDate()));
            stmt.setInt(4, filmDto.getDuration());
            stmt.setInt(5, filmDto.getRate());
            stmt.setInt(6, filmDto.getMpa().getId());
            return stmt;
        }, keyHolder);
        filmDto.setId(Objects.requireNonNull(keyHolder.getKey(),"Id нового фильма равен null").intValue());
        updateGenres(filmDto);
        updateDirectors(filmDto);
        return makeFilm(filmDto.getId());
    }

    @Override
    public Optional<FilmDto> updateDto(FilmDto filmDto) {
        if (getDtoById(filmDto.getId()).isPresent()) {
            jdbcTemplate.update(
                    "UPDATE FILMS " +
                            "SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, RATE = ?, MPA_ID = ?" +
                            "WHERE FILM_ID = ?",
                    filmDto.getName(), filmDto.getDescription(), filmDto.getReleaseDate(),
                    filmDto.getDuration(), filmDto.getRate(), filmDto.getMpa().getId(), filmDto.getId());
            updateGenres(filmDto);
            updateDirectors(filmDto);
            return Optional.of(makeFilm(filmDto.getId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<FilmDto> deleteDto(int filmId) {
        Optional<FilmDto> filmDto = getDtoById(filmId);
        if (filmDto.isPresent()) {
            jdbcTemplate.update("DELETE FROM FILMS WHERE film_id = ?", filmId);
            return filmDto;
        }
        return Optional.empty();
    }

    @Override
    public List<FilmDto> getAllDto() {
        return jdbcTemplate.query("SELECT * FROM FILMS F LEFT JOIN MPA M on F.MPA_ID = M.MPA_ID",
                filmRowMapper);
    }

    @Override
    public Optional<FilmDto> getDtoById(int id) {
        List<FilmDto> film = jdbcTemplate.query(
                "SELECT * FROM FILMS F LEFT JOIN MPA M on F.MPA_ID = M.MPA_ID WHERE FILM_ID = ?  ",
                filmRowMapper, id);
        return film.stream().findFirst();
    }

    @Override
    public Optional<FilmDto> setMetaValueDto(int dtoId, int metaId) {
        Optional<FilmDto> filmDto = getDtoById(dtoId);
        if (filmDto.isPresent()) {
            jdbcTemplate.update("MERGE INTO FILMS_LIKE (FILM_ID, USER_ID) VALUES (?,?)", dtoId, metaId);
            return filmDto;
        }
        return Optional.empty();
    }

    @Override
    public Optional<FilmDto> deleteMetaValueDto(int dtoId, int metaId) {
        Optional<FilmDto> filmDto = getDtoById(dtoId);
        if (filmDto.isPresent()) {
            jdbcTemplate.update("DELETE FROM FILMS_LIKE WHERE FILM_ID = ? AND USER_ID = ?", dtoId, metaId);
            return filmDto;
        }
        return Optional.empty();
    }

    @Override
    public List<FilmDto> getMetaValueDto(int count) {
        return jdbcTemplate.query(
                "SELECT F.*, M.MPA_NAME " +
                        "FROM FILMS AS F " +
                        "LEFT JOIN MPA M on F.MPA_ID = M.MPA_ID " +
                        "LEFT JOIN FILMS_LIKE AS FL ON FL.FILM_ID = F.FILM_ID " +
                        "GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(FL.USER_ID) DESC " +
                        "LIMIT ?;",
                filmRowMapper, count);

    }

    @Override
    public List<FilmDto> getMostPopular(int limit, String byGenre, String byYear) {
        return jdbcTemplate.query(
                "SELECT F.*, M.MPA_NAME " +
                        "FROM FILMS AS F " +
                        "LEFT JOIN MPA AS M on F.MPA_ID = M.MPA_ID " +
                        "LEFT JOIN FILMS_LIKE AS FL ON FL.FILM_ID = F.FILM_ID " +
                        "LEFT JOIN GENRES_FILM AS GF on F.FILM_ID = GF.FILM_ID " +
                        byYear +
                        "GROUP BY F.FILM_ID " +
                        byGenre +
                        "ORDER BY COUNT(FL.USER_ID) DESC " +
                        "LIMIT ?;",
                filmRowMapper, limit);
    }

    @Override
    public List<FilmDto> getCommonFilms(int userId, int friendId) {
        return jdbcTemplate.query(
                "SELECT  filz.FILM_ID,filz.NAME,filz.DESCRIPTION,filz.RELEASE_DATE," +
                        "filz.DURATION,filz.RATE,m.MPA_ID,m.MPA_NAME " +
                        "FROM (SELECT fil.*,fl_count.c " +
                        "FROM films as fil " +
                        "JOIN (SELECT fl2.FILM_ID, COUNT(*) as c " +
                        "FROM FILMS_LIKE as fl2 " +
                        "JOIN (SELECT u1.FILM_ID " +
                        "FROM  FILMS_LIKE as u1 " +
                        "JOIN (SELECT * FROM  FILMS_LIKE as fl) as u2 on u1.FILM_ID=u2.FILM_ID " +
                        "WHERE u1.USER_ID=? AND u2.USER_ID=?) as fl1 on fl2.FILM_ID=fl1.FILM_ID " +
                        "GROUP BY fl2.FILM_ID) as fl_count on fil.FILM_ID=fl_count.FILM_ID) as filz " +
                        "LEFT JOIN MPA as m on m.MPA_ID=filz.MPA_ID " +
                        "ORDER BY filz.c DESC;",
                filmRowMapper, userId, friendId);
    }

    @Override
    public List<FilmDto> getFilmsByDirectorSortByLikes(int directorId) {
        return jdbcTemplate.query(
                "SELECT f.*, m.*, listagg(g.genre_id) as genre_id,listagg(g.genre_name) as genre_name, " +
                        "listagg(d.director_id) as director_id,listagg(d.director_name) as director_name " +
                        "FROM films as f " +
                        "LEFT JOIN mpa AS m on f.mpa_id = m.mpa_id " +
                        "LEFT JOIN genres_film AS fg ON f.film_id = fg.film_id " +
                        "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                        "LEFT JOIN directors_film AS df ON f.film_id = df.film_id " +
                        "LEFT JOIN directors AS d ON df.director_id = d.director_id " +
                        "LEFT JOIN films_like AS l ON f.film_id = l.film_id " +
                        "WHERE df.director_id = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY COUNT (l.user_id) DESC",
                filmRowMapper, directorId);
    }

    @Override
    public List<FilmDto> getFilmsByDirectorSortByYear(int directorId) {
        return jdbcTemplate.query(
                "SELECT f.*, m.*, listagg(g.genre_id) as genre_id,listagg(g.genre_name) as genre_name, " +
                        "listagg(d.director_id) as director_id,listagg(d.director_name) as director_name " +
                        "FROM films as f " +
                        "LEFT JOIN mpa AS m on f.mpa_id = m.mpa_id " +
                        "LEFT JOIN genres_film AS fg ON f.film_id = fg.film_id " +
                        "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id " +
                        "LEFT JOIN directors_film AS df ON f.film_id = df.film_id " +
                        "LEFT JOIN directors AS d ON df.director_id = d.director_id " +
                        "WHERE df.director_id = ? " +
                        "GROUP BY f.film_id " +
                        "ORDER BY f.release_date",
                filmRowMapper, directorId);
    }

    private FilmDto makeFilm(int filmId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM FILMS F LEFT JOIN MPA M ON F.MPA_ID = M.MPA_ID WHERE FILM_ID = ?",
                filmRowMapper, filmId);
    }

    private void updateGenres(FilmDto filmDto) {
        jdbcTemplate.update("DELETE FROM GENRES_FILM WHERE FILM_ID = ?", filmDto.getId());
        if (!filmDto.getGenres().isEmpty()) {
            filmDto.getGenres()
                    .forEach(g -> jdbcTemplate.update("MERGE INTO GENRES_FILM (FILM_ID, GENRE_ID) VALUES (?,?)",
                            filmDto.getId(), g.getId()));
        }
    }

    private void updateDirectors(FilmDto filmDto) {
        jdbcTemplate.update("DELETE FROM directors_film WHERE FILM_ID = ?", filmDto.getId());
        if (!filmDto.getDirectors().isEmpty()) {
            filmDto.getDirectors()
                    .forEach(d -> jdbcTemplate.update("MERGE INTO directors_film (FILM_ID, director_id) VALUES (?,?)",
                            filmDto.getId(), d.getId()));
        }
    }

    @Override
    public List<FilmDto> getSearchFilms(String where, String whereOr) {
        return jdbcTemplate.query(
                "SELECT F.*, M.MPA_NAME\n" +
                        "FROM FILMS F\n" +
                        "LEFT JOIN MPA AS M on F.MPA_ID = M.MPA_ID " +
                        "LEFT JOIN FILMS_LIKE AS FL ON FL.FILM_ID = F.FILM_ID " +
                        "LEFT JOIN DIRECTORS_FILM DF on F.FILM_ID = DF.FILM_ID " +
                        "LEFT JOIN DIRECTORS D on DF.DIRECTOR_ID = D.DIRECTOR_ID " +
                        "WHERE " + where +
                        whereOr +
                        " GROUP BY F.FILM_ID " +
                        "ORDER BY COUNT(FL.USER_ID) DESC;", filmRowMapper);
    }
}