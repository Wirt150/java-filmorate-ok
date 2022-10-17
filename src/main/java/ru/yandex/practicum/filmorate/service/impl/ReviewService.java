package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DaoStorageFilm;
import ru.yandex.practicum.filmorate.dao.DaoStorageReview;
import ru.yandex.practicum.filmorate.dao.DaoStorageUser;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.ReviewDto;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceEvent;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;
import ru.yandex.practicum.filmorate.service.DtoServiceReview;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewService implements DtoServiceReview, DtoServiceRead<ReviewDto>, DtoServiceCrud<ReviewDto> {

    private final DaoStorageReview daoStorage;
    private final DaoStorageFilm daoStorageFilm;
    private final DaoStorageUser daoStorageUser;
    private final DtoServiceEvent dtoServiceEvent;


    public ReviewDto addDto(ReviewDto reviewDto) {
        if(reviewDto.getUserId() <= 0) {
            throw new IllegalArgumentException("Неверный id пользователя");
        }
        if(reviewDto.getFilmId() <= 0) {
            throw new IllegalArgumentException("Неверный id фильма");
        }
        ReviewDto review = daoStorage.addDto(reviewDto);
        if(review != null) {
            dtoServiceEvent.addReviewEvent(reviewDto.getUserId(), reviewDto.getReviewId());
        }
        return review;
    }

    public Optional<ReviewDto> updateDto(ReviewDto reviewDto) {
        Optional<ReviewDto> review = daoStorage.updateDto(reviewDto);
        if(review.isPresent()) {
            dtoServiceEvent.updateReviewEvent(review.get().getUserId(), review.get().getReviewId());
            return review;
        }
        throw new IllegalArgumentException("Отзыв с id " + reviewDto.getReviewId() + " не найден.");
    }

    public Optional<ReviewDto> deleteDto(int reviewId) {
        Optional<ReviewDto> review = daoStorage.deleteDto(reviewId);
        if(review.isPresent()) {
            dtoServiceEvent.removeReviewEvent(review.get().getUserId(), review.get().getReviewId());
            return review;
        }
        throw new IllegalArgumentException("Отзыв с id " + reviewId + " не найден.");
    }

    @Override
    public Optional<ReviewDto> getDtoById(int reviewId) {
        return Optional.of(daoStorage.getDtoById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Отзыв с id " + reviewId + " не найден.")));
    }

    @Override
    public List<ReviewDto> getAllDto() {
        return daoStorage.getAllDto();
    }

    @Override
    public void setLikeReview(int id, int userId) {
        Optional<ReviewDto> reviewDto = daoStorage.getDtoById(id);
        Optional<UserDto> userDto = daoStorageUser.getDtoById(userId);
        if (reviewDto.isPresent() && userDto.isPresent()) {
            daoStorage.setLikeReview(id, userId);
        } else {
            throw new IllegalArgumentException("Отзыв/юзер с id " + id + " не найден.");
        }
    }

    @Override
    public void setDislikeReview(int id, int userId) {
        Optional<ReviewDto> reviewDto = daoStorage.getDtoById(id);
        Optional<UserDto> userDto = daoStorageUser.getDtoById(userId);
        if (reviewDto.isPresent() && userDto.isPresent()) {
            daoStorage.setDislikeReview(id, userId);
        } else {
            throw new IllegalArgumentException("Отзыв/юзер с id " + id + " не найден.");
        }
    }

    @Override
    public void deleteLikeReview(int id, int userId) {
        Optional<ReviewDto> reviewDto = daoStorage.getDtoById(id);
        Optional<UserDto> userDto = daoStorageUser.getDtoById(userId);
        if (reviewDto.isPresent() && userDto.isPresent()) {
            daoStorage.deleteLikeReview(id, userId);
        } else {
            throw new IllegalArgumentException("Отзыв/юзер с id " + id + " не найден.");
        }
    }

    @Override
    public void deleteDislikeReview(int id, int userId) {
        Optional<ReviewDto> reviewDto = daoStorage.getDtoById(id);
        Optional<UserDto> userDto = daoStorageUser.getDtoById(userId);
        if (reviewDto.isPresent() && userDto.isPresent()) {
            daoStorage.deleteDislikeReview(id, userId);
        } else {
            throw new IllegalArgumentException("Отзыв/юзер с id " + id + " не найден.");
        }
    }

    @Override
    public List<ReviewDto> findAll(int filmId, int count) {
        if (filmId != 0) {
            Optional<FilmDto> filmDto = daoStorageFilm.getDtoById(filmId);
            if (filmDto.isEmpty()) {
                throw new IllegalArgumentException("Фильм с id " + filmId + " не найден.");
            }
        }
        return daoStorage.findAll(filmId, count);
    }
}