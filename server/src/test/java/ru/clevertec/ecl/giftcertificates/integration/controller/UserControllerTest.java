package ru.clevertec.ecl.giftcertificates.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.giftcertificates.annotation.ControllerTest;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.exception.model.IncorrectData;
import ru.clevertec.ecl.giftcertificates.integration.BaseIntegrationTest;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapper;
import ru.clevertec.ecl.giftcertificates.util.impl.UserTestBuilder;

@ControllerTest
@RequiredArgsConstructor
public class UserControllerTest extends BaseIntegrationTest {

    private final WebTestClient webTestClient;
    private final UserMapper userMapper;

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            long userId = 68L;
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchUserException",
                    "User with ID " + userId + " does not exist",
                    "404 NOT_FOUND"
            );

            webTestClient.get()
                    .uri("/users/" + userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should return expected json params and status 200")
        void testShouldReturnExpectedJsonParamsAndStatus200() {
            UserDto userDto = userMapper.toDto(UserTestBuilder.aUser().withUsername("Bully").build());

            webTestClient.get()
                    .uri("/users/" + userDto.id())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(userDto.id())
                    .jsonPath("$.username").isEqualTo(userDto.username())
                    .jsonPath("$.orders").isArray()
                    .jsonPath("$.orders").isNotEmpty();
        }

    }

    @Nested
    class FindAllGetEndpointTest {

        @Test
        @DisplayName("test should return json array with expected size and status 200")
        void testShouldReturnJsonArrayWithExpectedSizeAndStatus200() {
            int expectedSize = 5;

            webTestClient.get()
                    .uri("/users?page=0&size=10&sortBy=id")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$").isNotEmpty()
                    .jsonPath("$.size()").isEqualTo(expectedSize);
        }

        @Test
        @DisplayName("test should return empty json and status 200")
        void testShouldReturnEmptyJsonAndStatus200() {
            webTestClient.get()
                    .uri("/users?page=5&size=10&sortBy=id")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

    }

}
