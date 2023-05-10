package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.ecl.giftcertificates.annotation.ServiceTest;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.UserPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchUserException;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapper;
import ru.clevertec.ecl.giftcertificates.model.User;
import ru.clevertec.ecl.giftcertificates.repository.UserRepository;
import ru.clevertec.ecl.giftcertificates.service.UserService;
import ru.clevertec.ecl.giftcertificates.util.impl.UserPageRequestTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.UserTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ServiceTest
@RequiredArgsConstructor
class UserServiceImplTest {

    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private final UserMapper userMapper;
    private static final UserTestBuilder TEST_BUILDER = UserTestBuilder.aUser();

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test throw NoSuchUserException")
        void testThrowNoSuchUserException() {
            long id = 2L;

            doThrow(new NoSuchUserException(""))
                    .when(userRepository)
                    .findById(id);

            assertThrows(NoSuchUserException.class, () -> userService.findById(id));
        }

        @Test
        @DisplayName("test throw NoSuchUserException with expected message")
        void testThrowNoSuchUserExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "User with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchUserException.class,
                    () -> userService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected UserDto")
        void testShouldReturnExpectedUserDto() {
            User mockedUser = TEST_BUILDER.build();
            long id = mockedUser.getId();
            UserDto expectedValue = userMapper.toDto(mockedUser);

            doReturn(Optional.of(mockedUser))
                    .when(userRepository)
                    .findById(id);

            UserDto actualValue = userService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            User mockedUser = TEST_BUILDER.build();
            UserPageRequest request = UserPageRequestTestBuilder.aUserPageRequest().build();
            int expectedSize = 1;
            Page<User> page = new PageImpl<>(List.of(mockedUser));

            doReturn(page)
                    .when(userRepository)
                    .findAll(any(PageRequest.class));

            List<UserDto> actualValues = userService.findAll(request);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value")
        void testShouldReturnListThatContainsExpectedValue() {
            User mockedUser = TEST_BUILDER.build();
            UserPageRequest request = UserPageRequestTestBuilder.aUserPageRequest().build();
            Page<User> page = new PageImpl<>(List.of(mockedUser));

            doReturn(page)
                    .when(userRepository)
                    .findAll(any(PageRequest.class));

            List<UserDto> actualValues = userService.findAll(request);

            assertThat(actualValues).contains(userMapper.toDto(mockedUser));
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            UserPageRequest request = UserPageRequestTestBuilder.aUserPageRequest().build();

            doReturn(Page.empty())
                    .when(userRepository)
                    .findAll(any(PageRequest.class));

            List<UserDto> actualValues = userService.findAll(request);

            assertThat(actualValues).isEmpty();
        }

    }

}
