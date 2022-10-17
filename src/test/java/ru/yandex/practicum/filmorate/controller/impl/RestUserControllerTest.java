package ru.yandex.practicum.filmorate.controller.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.controller.RestAbstractControllerMetaValue;
import ru.yandex.practicum.filmorate.controller.RestControllerExtendedTest;
import ru.yandex.practicum.filmorate.model.UserDto;
import ru.yandex.practicum.filmorate.service.DtoServiceCrud;
import ru.yandex.practicum.filmorate.service.DtoServiceEvent;
import ru.yandex.practicum.filmorate.service.DtoServiceMetaValue;
import ru.yandex.practicum.filmorate.service.DtoServiceRead;
import ru.yandex.practicum.filmorate.service.DtoServiceUsers;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RestUserControllerTest extends RestControllerExtendedTest<UserDto> {
    @Autowired
    private final DtoServiceRead<UserDto> dtoServiceRead;
    @Autowired
    private final DtoServiceCrud<UserDto> dtoServiceCrud;
    @Autowired
    private final DtoServiceMetaValue<UserDto> dtoServiceMetaValue;
    @Autowired
    private final DtoServiceEvent dtoServiceEvent;

    @Autowired
    private final DtoServiceUsers userService;


    public static final UserDto USER_DTO_TEST = UserDto.builder()
            .id(0)
            .email("test@test.test")
            .login("login")
            .name("userDtoTest")
            .birthday(LocalDate.of(2000, Month.JANUARY, 1))
            .build();

    public static final UserDto USER_DTO_UPDATE_TEST = UserDto.builder()
            .id(1)
            .email("updateTest@test.test")
            .login("login")
            .name("userDtoUpdateTest")
            .birthday(LocalDate.of(2000, Month.JANUARY, 1))
            .build();

    @Override
    protected RestAbstractControllerMetaValue<UserDto> createRestController() {
        return new RestUserController(dtoServiceCrud,dtoServiceRead,dtoServiceMetaValue,dtoServiceEvent,userService);
    }

    @Override
    protected UserDto createDto() {
        return USER_DTO_TEST;
    }

    @Override
    protected UserDto createDtoTest() {
        return USER_DTO_UPDATE_TEST;
    }

    @Test
    @Override
    @DisplayName("Checking the PUT(setMetaValue) Method by controllers.")
    protected void whenCheckPutMetaValueMethod() {
        USER_DTO_UPDATE_TEST.setId(2);
        super.whenCheckPutMetaValueMethod();
    }

    @Test
    @Override
    @DisplayName("Checking the DELETE(deleteMetaValue) Method by controllers.")
    protected void whenCheckDeleteMetaValueMethod() {
        USER_DTO_UPDATE_TEST.setId(2);
        super.whenCheckDeleteMetaValueMethod();
    }

    @Test
    @Override
    @DisplayName("Checking the GET(getMetaValueDto) Method by controllers.")
    protected void whenCheckGetMetaValueMethod() {
        USER_DTO_UPDATE_TEST.setId(2);
        super.whenCheckGetMetaValueMethod();
    }

    @Test
    @DisplayName("Checking the GET(getMatchingFriend) Method by User controllers.")
    void whenCheckGetMatchingFriendMethod() {
        RestUserController userController = (RestUserController) restAbstractControllerMetaValue;
        USER_DTO_UPDATE_TEST.setId(2);

        final UserDto userFriendsTest = UserDto.builder()
                .id(3)
                .email("testFriends@test.test")
                .login("login")
                .name("userFriendsTest")
                .birthday(LocalDate.of(2000, Month.JANUARY, 1))
                .build();

        userController.create(USER_DTO_TEST, httpServletRequest);
        userController.create(USER_DTO_UPDATE_TEST, httpServletRequest);
        userController.create(userFriendsTest, httpServletRequest);

        restAbstractControllerMetaValue.setMetaValue(1, 2, httpServletRequest);
        restAbstractControllerMetaValue.setMetaValue(2, 1, httpServletRequest);
        restAbstractControllerMetaValue.setMetaValue(2, 3, httpServletRequest);
        restAbstractControllerMetaValue.setMetaValue(3, 2, httpServletRequest);


        List<UserDto> testUsersFriendsFirst = userController.getMatchingFriend(1, 2, httpServletRequest);
        List<UserDto> testUsersFriendsSecond = userController.getMatchingFriend(1, 3, httpServletRequest);
        Optional<UserDto> userDtoFriend = restAbstractControllerMetaValue.findById(2, httpServletRequest);

        //test
        assertEquals(testUsersFriendsFirst.size(), 0, "Размер должен быть равен 0.");
        assertEquals(testUsersFriendsSecond.size(), 1, "Размер должен быть равен 1.");
        assertEquals(testUsersFriendsSecond.get(0), userDtoFriend.get(), "Объекты должны быть равны.");
    }
}

