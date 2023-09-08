package ru.clevertec.ecl.giftcertificates.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.giftcertificates.annotation.ControllerTest;
import ru.clevertec.ecl.giftcertificates.dto.DeleteResponse;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.exception.model.IncorrectData;
import ru.clevertec.ecl.giftcertificates.integration.BaseIntegrationTest;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapper;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

@ControllerTest
@RequiredArgsConstructor
public class TagControllerTest extends BaseIntegrationTest {

    private final WebTestClient webTestClient;
    private final TagMapper tagMapper;

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            TagDto tagDto = tagMapper.toDto(TagTestBuilder.aTag().withName("Pepsi").build());

            webTestClient.get()
                    .uri("/tags/" + tagDto.id())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(TagDto.class)
                    .isEqualTo(tagDto);
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            int id = 87;
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchTagException",
                    "Tag with ID " + id + " does not exist",
                    "404 NOT_FOUND"
            );

            webTestClient.get()
                    .uri("/tags/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200")
        void testShouldReturnEmptyJsonAndStatus200() {
            webTestClient.get()
                    .uri("/tags?page=8&size=10&sortBy=id")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

        @Test
        @DisplayName("test should return json array with expected size and status 200")
        void testShouldReturnJsonArrayWithExpectedSizeAndStatus200() {
            int expectedSize = 5;

            webTestClient.get()
                    .uri("/tags?page=0&size=10&sortBy=id")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$").isNotEmpty()
                    .jsonPath("$.size()").isEqualTo(expectedSize);
        }

    }

    @Nested
    class SavePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() {
            TagDto tagDto = tagMapper.toDto(TagTestBuilder.aTag().withId(null).build());

            webTestClient.post()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(tagDto)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(6)
                    .jsonPath("$.name").isEqualTo("Bambucha");
        }

        @Test
        @DisplayName("test should return expected json and status 406")
        void testShouldReturnExpectedJsonAndStatus406() {
            TagDto tagDto = tagMapper.toDto(TagTestBuilder.aTag().withId(null).withName("Pepsi").build());
            IncorrectData expectedValue = new IncorrectData(
                    "NoTagWithTheSameNameException",
                    "Tag name " + tagDto.name() + " is already exist! It must be unique!",
                    "406 NOT_ACCEPTABLE"
            );

            webTestClient.post()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(tagDto)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdatePutEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            TagDto tagDto = tagMapper.toDto(TagTestBuilder.aTag().withId(4L).withName("Fire").build());

            webTestClient.put()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(tagDto)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(TagDto.class)
                    .isEqualTo(tagDto);
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            TagDto tagDto = tagMapper.toDto(TagTestBuilder.aTag().withId(87L).build());
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchTagException",
                    "Tag with ID " + tagDto.id() + " does not exist",
                    "404 NOT_FOUND"
            );

            webTestClient.put()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(tagDto)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class DeleteEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            long id = 5L;
            DeleteResponse response = new DeleteResponse("Tag with ID " + id + " was successfully deleted");

            webTestClient.delete()
                    .uri("/tags/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(DeleteResponse.class)
                    .isEqualTo(response);
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            long id = 17L;
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchTagException",
                    "There is no Tag with ID " + id + " to delete",
                    "404 NOT_FOUND"
            );

            webTestClient.delete()
                    .uri("/tags/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindTheMostWidelyUsedWithTheHighestCostGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            TagDto tagDto = tagMapper.toDto(TagTestBuilder.aTag().withId(3L).withName("Sprite").build());
            long userId = 1L;

            webTestClient.get()
                    .uri("/tags/user/" + userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(TagDto.class)
                    .isEqualTo(tagDto);
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            long userId = 4L;
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchTagException",
                    "There is no Tags in database with User ID " + userId + " connection",
                    "404 NOT_FOUND"
            );

            webTestClient.get()
                    .uri("/tags/user/" + userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

}
