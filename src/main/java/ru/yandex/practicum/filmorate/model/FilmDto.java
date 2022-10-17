package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ValidateDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilmDto {
    private static final int DESCRIPTION_LENGTH = 200;
    private int id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(max = DESCRIPTION_LENGTH, message = "Превышена максимальная длина описания в: " + DESCRIPTION_LENGTH + " символов.")
    private String description;
    @NotNull
    @ValidateDate(message = "Дата релиза не может быть раньше: 28 декабря 1895.")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной.")
    private int duration;
    private int rate;
    @NotNull(message = "Объект не может быть null.")
    private MpaDto mpa;
    @Builder.Default
    private List<GenreDto> genres = new ArrayList<>();
    @Builder.Default
    private List<DirectorDto> directors = new ArrayList<>();
}
