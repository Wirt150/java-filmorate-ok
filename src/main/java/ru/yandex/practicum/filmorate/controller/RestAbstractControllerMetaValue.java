package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceMetaValue;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.Optional;

@Slf4j
@Component
@Validated
public abstract class RestAbstractControllerMetaValue<T> extends RestAbstractControllerCrud<T> {
    protected final DtoServiceMetaValue<T> dtoServiceMetaValue;

    protected RestAbstractControllerMetaValue(
            DtoServiceCrud<T> dtoServiceCrud,
            DtoServiceRead<T> dtoServiceRead,
            DtoServiceMetaValue<T> dtoServiceMetaValue) {
        super(dtoServiceCrud, dtoServiceRead);
        this.dtoServiceMetaValue = dtoServiceMetaValue;
    }

    @PutMapping
    public Optional<T> setMetaValue(
            @Positive(message = "Id не может быть меньше 0.") final int dtoId,
            @Positive(message = "Id не может быть меньше 0.") final int metaId,
            HttpServletRequest request) {
        log.info(LOG_INFO + "На добавление информации", request.getRequestURI(), request.getMethod());
        return dtoServiceMetaValue.setMetaValueDto(dtoId, metaId);
    }

    @DeleteMapping
    public Optional<T> deleteMetaValue(
            @Positive(message = "Id не может быть меньше 0.") final int dtoId,
            @Positive(message = "Id не может быть меньше 0.") final int metaId,
            HttpServletRequest request) {
        log.info(LOG_INFO + "На удаление инфoрмации.", request.getRequestURI(), request.getMethod());
        return dtoServiceMetaValue.deleteMetaValueDto(dtoId, metaId);
    }
}

