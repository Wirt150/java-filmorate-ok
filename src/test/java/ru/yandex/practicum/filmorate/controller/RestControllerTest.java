package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.GenreDto;
import ru.yandex.practicum.filmorate.model.MpaDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class RestControllerTest<T> {

    @Autowired
    protected HttpServletRequest httpServletRequest;

    protected RestAbstractControllerRead<T> restAbstractController;
    protected T testDto;


    protected abstract RestAbstractControllerRead<T> createRestController();

    protected abstract T createDto();

    @BeforeEach
    void createController() {
        restAbstractController = createRestController();
        testDto = createDto();
    }

    @Test
    @DisplayName("Checking the GET(id) Method by controllers.")
    protected void whenCheckGetIdMethod() {
        Optional<T> test = restAbstractController.findById(1, httpServletRequest);

        //test
        assertEquals(test.get(), testDto, "Объекты должны быть равны.");
    }

    @Test
    @DisplayName("Checking the GET(all id) Method by controllers.")
    protected void whenCheckGetAllIdMethod() {
        List<T> testList = restAbstractController.findAll(httpServletRequest);

        //test
        if (testDto instanceof MpaDto) {
            assertEquals(testList.size(), 5, "Размер таблицы должен быть равен 5.");
        }

        if (testDto instanceof GenreDto) {
            assertEquals(testList.size(), 6, "Размер таблицы должен быть равен 6.");
        }
    }
}
