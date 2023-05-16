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
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.PriceDurationUpdateRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.ValidationErrorResponse;
import ru.clevertec.ecl.giftcertificates.exception.model.Violation;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;
import ru.clevertec.ecl.giftcertificates.util.impl.GiftCertificateTestBuilder;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ControllerTest
@RequiredArgsConstructor
class GiftCertificateControllerTest {

    @MockBean
    private GiftCertificateService giftCertificateService;
    private final WebTestClient webTestClient;
    private final GiftCertificateMapper giftCertificateMapper;
    private final ObjectMapper objectMapper;
    private static final GiftCertificateTestBuilder TEST_BUILDER = GiftCertificateTestBuilder.aGiftCertificate();

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            GiftCertificateResponse response = giftCertificateMapper.toResponse(TEST_BUILDER.build());

            doReturn(response)
                    .when(giftCertificateService)
                    .findById(response.id());

            webTestClient.get()
                    .uri("/gift-certificates/" + response.id())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GiftCertificateResponse.class)
                    .isEqualTo(response);
        }

    }

    @Nested
    class FindAllWithTagsGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200")
        void testShouldReturnEmptyJsonAndStatus200() {
            doReturn(List.of())
                    .when(giftCertificateService)
                    .findAllWithTags("Pepsi", "Litt", "date", "desc");

            webTestClient.get()
                    .uri("/gift-certificates?tagName=Pepsi&part=Litt&sortBy=date&order=desc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws JsonProcessingException {
            GiftCertificateResponse response = giftCertificateMapper.toResponse(TEST_BUILDER.build());
            String jsonArray = objectMapper.writeValueAsString(List.of(response));

            doReturn(List.of(response))
                    .when(giftCertificateService)
                    .findAllWithTags("Pepsi", "Litt", "date", "desc");

            webTestClient.get()
                    .uri("/gift-certificates?tagName=Pepsi&part=Litt&sortBy=date&order=desc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json(jsonArray);
        }

    }

    @Nested
    class SavePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() {
            GiftCertificateResponse response = giftCertificateMapper.toResponse(TEST_BUILDER.build());
            GiftCertificateRequest request = giftCertificateMapper.toRequest(TEST_BUILDER.build());

            doReturn(response)
                    .when(giftCertificateService)
                    .save(request);

            webTestClient.post()
                    .uri("/gift-certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(GiftCertificateResponse.class)
                    .isEqualTo(response);
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() {
            GiftCertificateRequest request
                    = giftCertificateMapper.toRequest(TEST_BUILDER.withPrice(BigDecimal.ZERO).build());
            ValidationErrorResponse response = new ValidationErrorResponse(
                    "409 CONFLICT",
                    List.of(new Violation("price", "Price must be greater than or equal to 0.01"))
            );

            webTestClient.post()
                    .uri("/gift-certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
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
            GiftCertificateResponse response = giftCertificateMapper.toResponse(TEST_BUILDER.build());
            PriceDurationUpdateRequest request =
                    new PriceDurationUpdateRequest(response.id(), response.price(), response.duration());

            doReturn(response)
                    .when(giftCertificateService)
                    .update(request);

            webTestClient.put()
                    .uri("/gift-certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(GiftCertificateResponse.class)
                    .isEqualTo(response);
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() {
            PriceDurationUpdateRequest request =
                    new PriceDurationUpdateRequest(1L, BigDecimal.TEN, 0);
            ValidationErrorResponse response = new ValidationErrorResponse(
                    "409 CONFLICT",
                    List.of(new Violation("duration", "Duration must be greater than 0"))
            );

            webTestClient.put()
                    .uri("/gift-certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
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
            DeleteResponse response = new DeleteResponse("GiftCertificate with ID " + id + " was successfully deleted");

            doNothing()
                    .when(giftCertificateService)
                    .delete(id);

            webTestClient.delete()
                    .uri("/gift-certificates/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(DeleteResponse.class)
                    .isEqualTo(response);
        }

    }

}
