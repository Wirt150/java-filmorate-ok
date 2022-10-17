package ru.yandex.practicum.filmorate.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.dao.DaoStorageFilm;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceFilm;
import ru.yandex.practicum.filmorate.service.DtoServiceMetaValue;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FilmService implements DtoServiceFilm, DtoServiceRead<FilmDto>, DtoServiceCrud<FilmDto>, DtoServiceMetaValue<FilmDto> {

    private static final String SKIP = "";
    private final DaoStorageFilm daoStorage;
    private final EventService eventService;

    @Override
    public FilmDto addDto(FilmDto filmDto) {
        return daoStorage.addDto(filmDto);
    }

    @Override
    public Optional<FilmDto> updateDto(FilmDto filmDto) {
        return Optional.of(daoStorage.updateDto(filmDto)
                .orElseThrow(() -> new IllegalArgumentException("Фильм c id: " + filmDto.getId() + " не найден.")));
    }

    @Override
    public Optional<FilmDto> deleteDto(int filmId) {
        return Optional.of(daoStorage.deleteDto(filmId)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с идентификатором " + filmId + " не найден.")));
    }

    @Override
    public Optional<FilmDto> getDtoById(int id) {
        return Optional.of(daoStorage.getDtoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с идентификатором " + id + " не найден.")));
    }

    @Override
    public List<FilmDto> getAllDto() {
        return daoStorage.getAllDto();
    }

    @Override
    public Optional<FilmDto> setMetaValueDto(final int dtoId, final int metaId) {
        Optional<FilmDto> filmDto = daoStorage.setMetaValueDto(dtoId, metaId);
        if (filmDto.isPresent()) {
            eventService.addLikeEvent(metaId, dtoId);
            return filmDto;
        }
        throw new IllegalArgumentException("Фильм с идентификатором " + dtoId + " не найден.");
    }

    @Override
    public Optional<FilmDto> deleteMetaValueDto(final int dtoId, final int metaId) {
        Optional<FilmDto> filmDto = daoStorage.deleteMetaValueDto(dtoId, metaId);
        if (filmDto.isPresent()) {
            eventService.removeLikeEvent(metaId, dtoId);
            return filmDto;
        }
        throw new IllegalArgumentException("Фильм с идентификатором " + dtoId + " не найден.");
    }

    @Override
    public List<FilmDto> getMetaValueDto(final int count) {
        return daoStorage.getMetaValueDto(count);
    }

    @Override
    public List<FilmDto> getMostPopular(final int limit, String genreId, final String year) {
        String genreSort = String.format("HAVING LISTAGG (GF.GENRE_ID) REGEXP '%s' ", "\\b" + genreId + "\\b");
        String yearSort = String.format("WHERE EXTRACT(YEAR FROM CAST(RELEASE_DATE AS date)) = %s ", year);
        if (StringUtils.hasText(genreId) && year == null) {
            return daoStorage.getMostPopular(limit, genreSort, SKIP);
        }
        if (StringUtils.hasText(year) && genreId == null) {
            return daoStorage.getMostPopular(limit, SKIP, yearSort);
        }
        return daoStorage.getMostPopular(limit, genreSort, yearSort);
    }

    @Override
    public List<FilmDto> getCommonFilms(final int userId, final int friendId) {
        return daoStorage.getCommonFilms(userId, friendId);
    }

    @Override
    public List<FilmDto> getFilmsByDirectorSortByLikes(int directorId) {
        List<FilmDto> sortedFilms = daoStorage.getFilmsByDirectorSortByLikes(directorId);
        if (sortedFilms.isEmpty()) {
            throw new IllegalArgumentException("Режиссер с идентификатором " + directorId + " не найден.");
        }
        return sortedFilms;
    }

    @Override
    public List<FilmDto> getFilmsByDirectorSortByYear(int directorId) {
        List<FilmDto> sortedFilms = daoStorage.getFilmsByDirectorSortByYear(directorId);
        if (sortedFilms.isEmpty()) {
            throw new IllegalArgumentException("Режиссер с идентификатором " + directorId + " не найден.");
        }
        return sortedFilms;
    }

    @Override
    public List<FilmDto> getSearchFilms(String query, List<String> by) {
        String queryBySql = "%" + query + "%";
        String titleSort = String.format("LOWER(F.NAME) LIKE '%s' ", queryBySql.toLowerCase());
        String directorSort = String.format("LOWER(D.DIRECTOR_NAME) LIKE '%s' ", queryBySql.toLowerCase());
        if (by.contains("title") && by.contains("director")) {
            return daoStorage.getSearchFilms(titleSort, "OR " + directorSort);
        }
        if (by.contains("title")) {
            return daoStorage.getSearchFilms(titleSort, SKIP);
        }
        return daoStorage.getSearchFilms(directorSort, SKIP);
    }
}