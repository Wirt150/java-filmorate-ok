package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerCrud;
import ru.yandex.practicum.filmorate.model.ReviewDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;
import ru.yandex.practicum.filmorate.service.impl.ReviewService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class RestReviewController extends RestAbstractControllerCrud<ReviewDto> {

    private final ReviewService reviewService;

    protected RestReviewController(
            DtoServiceCrud<ReviewDto> dtoServiceCrud,
            DtoServiceRead<ReviewDto> dtoServiceRead,
            ReviewService reviewService) {
        super(dtoServiceCrud, dtoServiceRead);
        this.reviewService = reviewService;
    }

    @PutMapping("{id}/like/{userId}")
    public void setLikeReview(
            @PathVariable("id") final @Positive(message = "Id не может быть меньше 0.") int dtoId,
            @PathVariable("userId") final @Positive(message = "Id не может быть меньше 0.") int userId,
            HttpServletRequest request) {
        logging(request);
        reviewService.setLikeReview(dtoId, userId);
    }

    @PutMapping("{id}/dislike/{userId}")
    public void setDislikeReview(
            @PathVariable("id") final @Positive(message = "Id не может быть меньше 0.") int dtoId,
            @PathVariable("userId") final @Positive(message = "Id не может быть меньше 0.") int userId,
            HttpServletRequest request) {
        logging(request);
        reviewService.setDislikeReview(dtoId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLikeReview(
            @PathVariable("id") final @Positive(message = "Id не может быть меньше 0.") int dtoId,
            @PathVariable("userId") final @Positive(message = "Id не может быть меньше 0.") int userId,
            HttpServletRequest request) {
        logging(request);
        reviewService.deleteLikeReview(dtoId, userId);
    }

    @DeleteMapping("{id}/dislike/{userId}")
    public void deleteDislikeReview(
            @PathVariable("id") final @Positive(message = "Id не может быть меньше 0.") int dtoId,
            @PathVariable("userId") final @Positive(message = "Id не может быть меньше 0.") int userId,
            HttpServletRequest request) {
        logging(request);
        reviewService.deleteDislikeReview(dtoId, userId);
    }

    @GetMapping(params = {"filmId"})
    public List<ReviewDto> findAll(
            @RequestParam(defaultValue = "0", required = false) @Positive(message = "Id не может быть меньше 0.") int filmId,
            @RequestParam(defaultValue = "10", required = false) @Positive(message = "count не может быть меньше 0.") int count,
            HttpServletRequest request) {
        logging(request);
        return reviewService.findAll(filmId, count);
    }
}
