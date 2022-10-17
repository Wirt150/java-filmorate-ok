package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import ru.yandex.practicum.filmorate.model.FilmDto;
import ru.yandex.practicum.filmorate.model.UserDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class RestControllerExtendedTest<T> {

    @Autowired
    protected HttpServletRequest httpServletRequest;
    protected RestAbstractControllerMetaValue<T> restAbstractControllerMetaValue;
    protected T testDto;
    protected T testDtoUpdate;

    protected abstract RestAbstractControllerMetaValue<T> createRestController();

    protected abstract T createDto();

    protected abstract T createDtoTest();

    @BeforeEach
    void createController() {
        restAbstractControllerMetaValue = createRestController();
        testDto = createDto();
        testDtoUpdate = createDtoTest();
    }

    @Test
    @DisplayName("Checking the POST Method by controllers.")
    protected void whenCheckCreateMethod() {
        restAbstractControllerMetaValue.create(testDto, httpServletRequest);
        List<T> dtoList = restAbstractControllerMetaValue.findAll(httpServletRequest);

        if (testDto instanceof UserDto) {
            ((UserDto) testDto).setId(1);
        }

        //test
        assertEquals(1, dtoList.size(), "Размер списка должен быть равен 1.");
        assertEquals(testDto, dtoList.get(0), "Объекты должны быть равны");
    }

    @Test
    @DisplayName("Checking the PUT Method by controllers.")
    protected void whenCheckUpdateMethod() {
        restAbstractControllerMetaValue.create(testDto, httpServletRequest);
        restAbstractControllerMetaValue.update(testDtoUpdate, httpServletRequest);
        List<T> dtoList = restAbstractControllerMetaValue.findAll(httpServletRequest);

        //test
        assertEquals(1, dtoList.size(), "Размер списка должен быть равен 1.");
        assertNotEquals(testDto, dtoList.get(0), "Объекты не должны быть равны");
        assertEquals(testDtoUpdate, dtoList.get(0), "Объекты должны быть равны");
    }

    @Test
    @DisplayName("Checking the GET(id) Method by controllers.")
    protected void whenCheckGetIdMethod() {
        restAbstractControllerMetaValue.create(testDto, httpServletRequest);
        Optional<T> testOptionalByMethod = restAbstractControllerMetaValue.findById(1, httpServletRequest);
        T testDtoByMethod = testOptionalByMethod.get();

        if (testDto instanceof UserDto) {
            ((UserDto) testDto).setId(1);
        }

        //test
        assertEquals(testDtoByMethod, testDto, "Объекты должны быть равны.");
    }

    @Test
    @DisplayName("Checking the PUT(setMetaValue) Method by controllers.")
    protected void whenCheckPutMetaValueMethod() {
        restAbstractControllerMetaValue.create(testDto, httpServletRequest);
        restAbstractControllerMetaValue.create(testDtoUpdate, httpServletRequest);
        restAbstractControllerMetaValue.setMetaValue(1, 2, httpServletRequest);

        Optional<T> testOptionalByMethod = restAbstractControllerMetaValue.findById(1, httpServletRequest);
        T testDtoByMethod = testOptionalByMethod.get();

        //test
        if (testDtoByMethod instanceof UserDto) {
            List<T> testTable = restAbstractControllerMetaValue.dtoServiceMetaValue.getMetaValueDto(1);

            assertEquals(testTable.size(), 1, "Размер должен быть равен 1.");

            List<T> testTableFriend = restAbstractControllerMetaValue.dtoServiceMetaValue.getMetaValueDto(2);

            assertEquals(testTableFriend.size(), 0, "Размер должен быть равен 0.");
        }
        if (testDtoByMethod instanceof FilmDto) {
            List<T> testTable = restAbstractControllerMetaValue.dtoServiceMetaValue.getMetaValueDto(1);
            assertEquals(testTable.size(), 1, "Размер должен быть равен 1.");
        }
    }

    @Test
    @DisplayName("Checking the DELETE(deleteMetaValue) Method by controllers.")
    protected void whenCheckDeleteMetaValueMethod() {
        restAbstractControllerMetaValue.create(testDto, httpServletRequest);
        restAbstractControllerMetaValue.create(testDtoUpdate, httpServletRequest);
        restAbstractControllerMetaValue.setMetaValue(1, 2, httpServletRequest);
        restAbstractControllerMetaValue.deleteMetaValue(1, 2, httpServletRequest);

        Optional<T> testOptionalByMethod = restAbstractControllerMetaValue.findById(1, httpServletRequest);
        T testDtoByMethod = testOptionalByMethod.get();

        //test
        if (testDtoByMethod instanceof UserDto) {
            List<T> testTable = restAbstractControllerMetaValue.dtoServiceMetaValue.getMetaValueDto(1);

            assertEquals(testTable.size(), 0, "Размер должен быть равен 1.");

            List<T> testTableFriend = restAbstractControllerMetaValue.dtoServiceMetaValue.getMetaValueDto(2);

            assertEquals(testTableFriend.size(), 0, "Размер должен быть равен 0.");
        }
        if (testDtoByMethod instanceof FilmDto) {
            List<T> testTable = restAbstractControllerMetaValue.dtoServiceMetaValue.getMetaValueDto(1);
            assertEquals(testTable.size(), 1, "Размер должен быть равен 1.");
        }
    }

    @Test
    @DisplayName("Checking the GET(getMetaValueDto) Method by controllers.")
    protected void whenCheckGetMetaValueMethod() {
        restAbstractControllerMetaValue.create(testDto, httpServletRequest);
        restAbstractControllerMetaValue.create(testDtoUpdate, httpServletRequest);
        restAbstractControllerMetaValue.setMetaValue(1, 2, httpServletRequest);

        List<T> testListDtoByMethod = restAbstractControllerMetaValue.dtoServiceMetaValue.getMetaValueDto(1);

        //test
        if (testListDtoByMethod.get(0) instanceof UserDto) {
            Optional<T> testDto = restAbstractControllerMetaValue.findById(2, httpServletRequest);
            assertEquals(testListDtoByMethod.size(), 1, "Размер должен быть равен 1.");
            assertEquals(testListDtoByMethod.get(0), testDto.get(), "Объекты должны быть равны.");
        }
        if (testListDtoByMethod.get(0) instanceof FilmDto) {
            Optional<T> testDto = restAbstractControllerMetaValue.findById(1, httpServletRequest);
            assertEquals(testListDtoByMethod.size(), 1, "Размер должен быть равен 2.");
            assertEquals(testListDtoByMethod.get(0), testDto.get(), "Объекты должны быть равны.");
        }
    }
}
