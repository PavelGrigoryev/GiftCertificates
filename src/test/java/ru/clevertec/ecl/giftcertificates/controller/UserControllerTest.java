package ru.clevertec.ecl.giftcertificates.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.giftcertificates.annotation.ControllerTest;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.UserPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.ValidationErrorResponse;
import ru.clevertec.ecl.giftcertificates.exception.model.Violation;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapper;
import ru.clevertec.ecl.giftcertificates.service.UserService;
import ru.clevertec.ecl.giftcertificates.util.impl.UserTestBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@ControllerTest
@RequiredArgsConstructor
class UserControllerTest {

    @MockBean
    private UserService userService;
    private final WebTestClient webTestClient;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private static final UserTestBuilder TEST_BUILDER = UserTestBuilder.aUser();

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            UserDto userDto = userMapper.toDto(TEST_BUILDER.build());

            doReturn(userDto)
                    .when(userService)
                    .findById(userDto.id());

            webTestClient.get()
                    .uri("/users/" + userDto.id())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDto.class)
                    .isEqualTo(userDto);
        }

    }

    @Nested
    class FindAllGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200")
        void testShouldReturnEmptyJsonAndStatus200() {
            doReturn(List.of())
                    .when(userService)
                    .findAll(any(UserPageRequest.class));

            webTestClient.get()
                    .uri("/users?page=0&size=10&sortBy=id")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws JsonProcessingException {
            UserDto userDto = userMapper.toDto(TEST_BUILDER.build());
            String jsonArray = objectMapper.writeValueAsString(List.of(userDto));

            doReturn(List.of(userDto))
                    .when(userService)
                    .findAll(any(UserPageRequest.class));

            webTestClient.get()
                    .uri("/users?page=0&size=10&sortBy=id")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json(jsonArray);
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() {
            ValidationErrorResponse response = new ValidationErrorResponse(
                    "409 CONFLICT",
                    List.of(new Violation("sortBy", "Acceptable values are only: id or username"))
            );

            webTestClient.get()
                    .uri("/users?page=0&size=10&sortBy=price")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                    .expectBody(ValidationErrorResponse.class)
                    .isEqualTo(response);
        }

    }

}
