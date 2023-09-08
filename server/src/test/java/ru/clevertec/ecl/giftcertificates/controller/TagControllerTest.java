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
import ru.clevertec.ecl.giftcertificates.dto.DeleteResponse;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.TagPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.ValidationErrorResponse;
import ru.clevertec.ecl.giftcertificates.exception.model.Violation;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapper;
import ru.clevertec.ecl.giftcertificates.service.TagService;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ControllerTest
@RequiredArgsConstructor
class TagControllerTest {

    @MockBean
    private TagService tagService;
    private final WebTestClient webTestClient;
    private final TagMapper tagMapper;
    private final ObjectMapper objectMapper;
    private static final TagTestBuilder TEST_BUILDER = TagTestBuilder.aTag();

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            TagDto tagDto = tagMapper.toDto(TEST_BUILDER.build());

            doReturn(tagDto)
                    .when(tagService)
                    .findById(tagDto.id());

            webTestClient.get()
                    .uri("/tags/" + tagDto.id())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(TagDto.class)
                    .isEqualTo(tagDto);
        }

    }

    @Nested
    class FindAllGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200")
        void testShouldReturnEmptyJsonAndStatus200() {
            doReturn(List.of())
                    .when(tagService)
                    .findAll(any(TagPageRequest.class));

            webTestClient.get()
                    .uri("/tags?page=0&size=10&sortBy=id")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws JsonProcessingException {
            TagDto tagDto = tagMapper.toDto(TEST_BUILDER.build());
            String jsonArray = objectMapper.writeValueAsString(List.of(tagDto));

            doReturn(List.of(tagDto))
                    .when(tagService)
                    .findAll(any(TagPageRequest.class));

            webTestClient.get()
                    .uri("/tags?page=0&size=10&sortBy=id")
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
                    List.of(new Violation("page", "Page must be greater than or equal to 0"))
            );

            webTestClient.get()
                    .uri("/tags?page=-1&size=10&sortBy=id")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                    .expectBody(ValidationErrorResponse.class)
                    .isEqualTo(response);
        }

    }

    @Nested
    class SavePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() {
            TagDto tagDto = tagMapper.toDto(TEST_BUILDER.build());

            doReturn(tagDto)
                    .when(tagService)
                    .save(tagDto);

            webTestClient.post()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(tagDto)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(TagDto.class)
                    .isEqualTo(tagDto);
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() {
            TagDto tagDto = tagMapper.toDto(TEST_BUILDER.withName(" ").build());
            ValidationErrorResponse response = new ValidationErrorResponse(
                    "409 CONFLICT",
                    List.of(new Violation("name", "Name cannot be blank"))
            );

            webTestClient.post()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(tagDto)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                    .expectBody(ValidationErrorResponse.class)
                    .isEqualTo(response);
        }

    }

    @Nested
    class UpdatePutEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            TagDto tagDto = tagMapper.toDto(TEST_BUILDER.build());

            doReturn(tagDto)
                    .when(tagService)
                    .update(tagDto);

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
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() {
            TagDto tagDto = tagMapper.toDto(TEST_BUILDER.withName(" ").build());
            ValidationErrorResponse response = new ValidationErrorResponse(
                    "409 CONFLICT",
                    List.of(new Violation("name", "Name cannot be blank"))
            );

            webTestClient.put()
                    .uri("/tags")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(tagDto)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                    .expectBody(ValidationErrorResponse.class)
                    .isEqualTo(response);
        }

    }

    @Nested
    class DeleteEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            long id = 1L;
            DeleteResponse response = new DeleteResponse("Tag with ID " + id + " was successfully deleted");

            doNothing()
                    .when(tagService)
                    .delete(id);

            webTestClient.delete()
                    .uri("/tags/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(DeleteResponse.class)
                    .isEqualTo(response);
        }

    }

    @Nested
    class FindTheMostWidelyUsedWithTheHighestCostGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            TagDto tagDto = tagMapper.toDto(TEST_BUILDER.build());
            long userId = 1L;

            doReturn(tagDto)
                    .when(tagService)
                    .findTheMostWidelyUsedWithTheHighestCost(userId);

            webTestClient.get()
                    .uri("/tags/user/" + userId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(TagDto.class)
                    .isEqualTo(tagDto);
        }

    }

}
