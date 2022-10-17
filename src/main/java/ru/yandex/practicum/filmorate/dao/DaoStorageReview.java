package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.ReviewDto;

import java.util.List;

public interface DaoStorageReview extends DaoStorageCrud<ReviewDto> {

    List<ReviewDto> findAll(int filmId, int count);

    void setLikeReview(int id, int userId);

    void setDislikeReview(int id, int userId);

    void deleteLikeReview(int id, int userId);

    void deleteDislikeReview(int id, int userId);
}
