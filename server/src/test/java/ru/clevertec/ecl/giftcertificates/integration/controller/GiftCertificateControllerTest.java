package ru.clevertec.ecl.giftcertificates.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.clevertec.ecl.giftcertificates.annotation.ControllerTest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.PriceDurationUpdateRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.IncorrectData;
import ru.clevertec.ecl.giftcertificates.integration.BaseIntegrationTest;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.giftcertificates.util.impl.GiftCertificateTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.math.BigDecimal;
import java.util.List;

@ControllerTest
@RequiredArgsConstructor
public class GiftCertificateControllerTest extends BaseIntegrationTest {

    private final WebTestClient webTestClient;
    private final GiftCertificateMapper giftCertificateMapper;

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json params and status 200")
        void testShouldReturnExpectedJsonParamsAndStatus200() {
            int id = 3;

            webTestClient.get()
                    .uri("/gift-certificates/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.name").isEqualTo("Pechkin")
                    .jsonPath("$.description").isEqualTo("abda");
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonParamsAndStatus404() {
            int id = 99;
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchGiftCertificateException",
                    "GiftCertificate with ID " + id + " does not exist",
                    "404 NOT_FOUND"
            );

            webTestClient.get()
                    .uri("/gift-certificates/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllWithTagsGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200")
        void testShouldReturnEmptyJsonAndStatus200() {
            webTestClient.get()
                    .uri("/gift-certificates?tagName=Pep")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

        @Test
        @DisplayName("test should return not empty json array and status 200")
        void testShouldReturnNotEmptyJsonArrayAndStatus200() {
            webTestClient.get()
                    .uri("/gift-certificates?tagName=7-Up&sortBy=date&order=desc")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$").isArray()
                    .jsonPath("$").isNotEmpty();
        }

    }

    @Nested
    class SavePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 406")
        void testShouldReturnExpectedJsonAndStatus406() {
            GiftCertificateRequest request =
                    giftCertificateMapper.toRequest(GiftCertificateTestBuilder.aGiftCertificate().withId(null)
                            .withTags(List.of(TagTestBuilder.aTag().build(), TagTestBuilder.aTag().build()))
                            .build());
            IncorrectData expectedValue = new IncorrectData(
                    "NoTagWithTheSameNameException",
                    "There should be no tags with the same name!",
                    "406 NOT_ACCEPTABLE"
            );

            webTestClient.post()
                    .uri("/gift-certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdatePutEndpointTest {

        @Test
        @DisplayName("test should return expected json params and status 200")
        void testShouldReturnExpectedJsonParamsAndStatus200() {
            PriceDurationUpdateRequest request =
                    new PriceDurationUpdateRequest(7L, BigDecimal.TEN, 99);

            webTestClient.put()
                    .uri("/gift-certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.id").isEqualTo(7)
                    .jsonPath("$.price").isEqualTo(10)
                    .jsonPath("$.duration").isEqualTo(99);
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            PriceDurationUpdateRequest request =
                    new PriceDurationUpdateRequest(77L, BigDecimal.TEN, 99);
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchGiftCertificateException",
                    "GiftCertificate with ID " + request.id() + " does not exist",
                    "404 NOT_FOUND"
            );

            webTestClient.put()
                    .uri("/gift-certificates")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class DeleteEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            long id = 111L;
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchGiftCertificateException",
                    "There is no GiftCertificate with ID " + id + " to delete",
                    "404 NOT_FOUND"
            );

            webTestClient.delete()
                    .uri("/gift-certificates/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

}
