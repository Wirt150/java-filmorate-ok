package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.DaoStorageReview;
import ru.yandex.practicum.filmorate.mapper.ReviewRowMapper;
import ru.yandex.practicum.filmorate.model.ReviewDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DaoReview implements DaoStorageReview {
    private static final String[] REVIEW_COLUMN_NAMES = {"REVIEW_ID"};
    private final JdbcTemplate jdbcTemplate;
    private final ReviewRowMapper reviewRowMapper;

    @Override
    public ReviewDto addDto(ReviewDto reviewDto) {
        String sql = "INSERT INTO REVIEW(CONTENT, IS_POSITIVE, USER_ID, FILM_ID, USEFUL) VALUES ( ?,?,?,?,? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, REVIEW_COLUMN_NAMES);
            stmt.setString(1, reviewDto.getContent());
            stmt.setBoolean(2, reviewDto.getIsPositive());
            stmt.setInt(3, reviewDto.getUserId());
            stmt.setInt(4, reviewDto.getFilmId());
            stmt.setInt(5, reviewDto.getUseful());
            return stmt;
        }, keyHolder);
        try {
            reviewDto.setReviewId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        } catch (Throwable e) {
            throw new NullPointerException("Id нового отзыва равен null");
        }
        return makeReview(reviewDto.getReviewId());
    }

    @Override
    public Optional<ReviewDto> updateDto(ReviewDto reviewDto) {
        if (getDtoById(reviewDto.getReviewId()).isPresent()) {
            jdbcTemplate.update(
                    "UPDATE REVIEW " +
                            "SET CONTENT = ?, IS_POSITIVE = ?" +
                            "WHERE REVIEW_ID = ?",
                    reviewDto.getContent(), reviewDto.getIsPositive(), reviewDto.getReviewId());
            return Optional.of(makeReview(reviewDto.getReviewId()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ReviewDto> deleteDto(int id) {
        Optional<ReviewDto> reviewDto = getDtoById(id);
        if (reviewDto.isPresent()) {
            jdbcTemplate.update("DELETE FROM REVIEW WHERE REVIEW_ID = ?", id);
            return reviewDto;
        }
        return Optional.empty();
    }

    @Override
    public List<ReviewDto> getAllDto() {
        return jdbcTemplate.query("SELECT * FROM REVIEW ORDER BY USEFUL DESC ", reviewRowMapper);
    }

    @Override
    public Optional<ReviewDto> getDtoById(int id) {
        String sqlQuery = "SELECT * FROM REVIEW WHERE REVIEW_ID = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sqlQuery, reviewRowMapper, id));
        } catch (DataAccessException e) {
            throw new IllegalArgumentException("Отзыв с id " + id + " не найден");
        }
    }

    @Override
    public List<ReviewDto> findAll(int filmId, int count) {
        List<ReviewDto> listReview = jdbcTemplate.query(
                "SELECT * " +
                        "FROM REVIEW " +
                        "ORDER BY USEFUL DESC " +
                        "LIMIT ?",
                reviewRowMapper, count);
        if (filmId != 0) {
            listReview = listReview.stream()
                    .filter(r -> r.getFilmId() == filmId)
                    .collect(Collectors.toList());
        }
        return listReview;
    }

    @Override
    public void setLikeReview(int id, int userId) {
        List<Integer> userLikes = getReviewLikesFromTable(id);
        if (!userLikes.contains(userId)) {
            List<Integer> userDisLikes = getReviewDislikesFromTable(id);
            if (userDisLikes.contains(userId)) {
                deleteDislike(id, userId);
            }
            jdbcTemplate.update("INSERT INTO REVIEW_LIKES (REVIEW_ID, USER_ID) VALUES ( ?,? )", id, userId);
            jdbcTemplate.update("UPDATE REVIEW SET USEFUL = USEFUL + 1 WHERE REVIEW_ID = ?", id);
        }
    }

    @Override
    public void setDislikeReview(int id, int userId) {
        List<Integer> userDislikes = getReviewDislikesFromTable(id);
        if (!userDislikes.contains(userId)) {
            List<Integer> userLikes = getReviewLikesFromTable(id);
            if (userLikes.contains(userId)) {
                deleteLike(id, userId);
            }
            jdbcTemplate.update("INSERT INTO REVIEW_DISLIKES (REVIEW_ID, USER_ID) VALUES ( ?,? )", id, userId);
            jdbcTemplate.update("UPDATE REVIEW SET USEFUL = USEFUL - 1 WHERE REVIEW_ID = ?", id);
        }
    }

    @Override
    public void deleteLikeReview(int id, int userId) {
        deleteLike(id, userId);
        jdbcTemplate.update("UPDATE REVIEW SET USEFUL = USEFUL - 1 WHERE REVIEW_ID = ?", id);
    }

    @Override
    public void deleteDislikeReview(int id, int userId) {
        deleteDislike(id, userId);
        jdbcTemplate.update("UPDATE REVIEW SET USEFUL = USEFUL + 1 WHERE REVIEW_ID = ?", id);
    }

    private void deleteLike(int id, int userId) {
        String sqlQuery = "DELETE FROM REVIEW_LIKES WHERE REVIEW_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    private void deleteDislike(int id, int userId) {
        String sqlQuery = "DELETE FROM REVIEW_DISLIKES WHERE REVIEW_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
    }

    private List<Integer> getReviewLikesFromTable(int reviewId) {
        String sqlQuery = "SELECT USER_ID FROM REVIEW_LIKES WHERE REVIEW_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::getUserIdFromTableReviewLikes, reviewId);
    }

    private Integer getUserIdFromTableReviewLikes(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("USER_ID");
    }

    private List<Integer> getReviewDislikesFromTable(int reviewId) {
        String sqlQuery = "SELECT USER_ID FROM REVIEW_DISLIKES WHERE REVIEW_ID = ?";
        return jdbcTemplate.query(sqlQuery, this::getUserIdFromTableReviewDisLikes, reviewId);
    }

    private Integer getUserIdFromTableReviewDisLikes(ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getInt("USER_ID");
    }

    private ReviewDto makeReview(int reviewId) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM REVIEW WHERE REVIEW_ID = ?",
                reviewRowMapper, reviewId);
    }

}