package ru.yandex.practicum.filmorate.controller.impl;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerMetaValue;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.model.event.EventDto;
import ru.yandex.practicum.filmorate.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/users")
public class RestUserController extends RestAbstractControllerMetaValue<UserDto> {

    private final DtoServiceEvent dtoServiceEvent;

    private final DtoServiceUsers userService;

    protected RestUserController(
            DtoServiceCrud<UserDto> dtoServiceCrud,
            DtoServiceRead<UserDto> dtoServiceRead,
            DtoServiceMetaValue<UserDto> dtoServiceMetaValue,
            DtoServiceEvent dtoServiceEvent,
            DtoServiceUsers userService) {
        super(dtoServiceCrud, dtoServiceRead, dtoServiceMetaValue);
        this.dtoServiceEvent = dtoServiceEvent;
        this.userService = userService;
    }

    @Override
    @PutMapping("{id}/friends/{friendId}")
    public Optional<UserDto> setMetaValue(
            @PathVariable("id") final @Positive(message = "Id не может быть меньше 0.") int dtoId,
            @PathVariable("friendId") final @Positive(message = "Id не может быть меньше 0.") int metaId,
            HttpServletRequest request) {
        return super.setMetaValue(dtoId, metaId, request);
    }

    @Override
    @DeleteMapping("{id}/friends/{friendId}")
    public Optional<UserDto> deleteMetaValue(
            @PathVariable("id") final @Positive(message = "Id не может быть меньше 0.") int dtoId,
            @PathVariable("friendId") final @Positive(message = "Id не может быть меньше 0.") int metaId,
            HttpServletRequest request) {
        return super.deleteMetaValue(dtoId, metaId, request);
    }

    @GetMapping("{id}/friends")
    public List<UserDto> getMetaValueDto(@PathVariable final int id, HttpServletRequest request) {
        logging(request);
        return dtoServiceMetaValue.getMetaValueDto(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    public List<UserDto> getMatchingFriend(
            @PathVariable final int id,
            @PathVariable final int otherId,
            HttpServletRequest request
    ) {
        logging(request);
        return dtoServiceMetaValue.getMetaValueDto(id).stream()
                .filter(dtoServiceMetaValue.getMetaValueDto(otherId)::contains)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}/recommendations")
    public List<FilmDto> recommendations(@PathVariable final int id, HttpServletRequest request) {
        logging(request);
        return userService.recommendations(id);
    }

    @GetMapping("{id}/feed")
    public List<EventDto> getUserFeed(@PathVariable int id, HttpServletRequest request) {
        logging(request);
        return dtoServiceEvent.getUserEvents(id);
    }
}
