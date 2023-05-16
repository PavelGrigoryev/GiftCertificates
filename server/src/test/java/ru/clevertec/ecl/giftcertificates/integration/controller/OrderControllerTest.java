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
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.UpdateYourOrderRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.IncorrectData;
import ru.clevertec.ecl.giftcertificates.integration.BaseIntegrationTest;

import java.util.List;

@ControllerTest
@RequiredArgsConstructor
public class OrderControllerTest extends BaseIntegrationTest {

    private final WebTestClient webTestClient;

    @Nested
    class MakeAnOrderPostEndpointTest {

        @Test
        @DisplayName("test should return json with expected price and status 201")
        void testShouldReturnJsonWithExpectedPriceAndStatus201() {
            int expectedPrice = 100;
            MakeAnOrderRequest request = new MakeAnOrderRequest(5L, List.of(2L, 6L));

            webTestClient.post()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody()
                    .jsonPath("$.price").isEqualTo(expectedPrice);
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            MakeAnOrderRequest request = new MakeAnOrderRequest(89L, List.of(2L, 6L));
            IncorrectData expectedValue = new IncorrectData(
                    "NoSuchUserException",
                    "User with ID " + request.userId() + " does not exist",
                    "404 NOT_FOUND"
            );

            webTestClient.post()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllUserOrdersGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200")
        void testShouldReturnEmptyJsonAndStatus200() {
            webTestClient.get()
                    .uri("/orders/2?page=8&size=10&sortBy=price")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

        @Test
        @DisplayName("test should return json array with expected size and status 200")
        void testShouldReturnJsonArrayWithExpectedSizeAndStatus200() {
            int expectedSize = 3;

            webTestClient.get()
                    .uri("/orders/1?page=0&size=10&sortBy=id")
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
    class UpdateUserOrderPutEndpointTest {

        @Test
        @DisplayName("test should return expected json params and status 200")
        void testShouldReturnExpectedJsonParamsAndStatus200() {
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, 2L, List.of(3L, 4L));

            webTestClient.put()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .jsonPath("$.gift_certificates.size()").isEqualTo(4L)
                    .jsonPath("$.gift_certificates[2].name").isEqualTo("Pechkin")
                    .jsonPath("$.gift_certificates[3].description").isEqualTo("Zero")
                    .jsonPath("$.gift_certificates[3].tags.size()").isEqualTo(4)
                    .jsonPath("$.gift_certificates[3].tags[3].name").isEqualTo("Cola");
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(3L, 4L, List.of(2L, 6L));
            IncorrectData expectedValue = new IncorrectData(
                    "NoRelationBetweenOrderAndUserException",
                    "User with ID " + request.userId() + " does not have such Order with ID " + request.orderId(),
                    "404 NOT_FOUND"
            );

            webTestClient.put()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should return expected json and status 406")
        void testShouldReturnExpectedJsonAndStatus406() {
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, 3L, List.of(2L));
            IncorrectData expectedValue = new IncorrectData(
                    "AlreadyHaveThisCertificateException",
                    "Your already have this GiftCertificate",
                    "406 NOT_ACCEPTABLE"
            );

            webTestClient.put()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.NOT_ACCEPTABLE)
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

    @Nested
    class DeleteUserOrderDeleteEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            long orderId = 4L;
            long userId = 2L;
            DeleteResponse response = new DeleteResponse("Order with ID " + orderId
                                                         + " for User with ID " + userId + " was successfully deleted");

            webTestClient.delete()
                    .uri("/orders?userId=" + userId + "&orderId=" + orderId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(DeleteResponse.class)
                    .isEqualTo(response);
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() {
            long orderId = 1L;
            long userId = 2L;
            IncorrectData expectedValue = new IncorrectData(
                    "NoRelationBetweenOrderAndUserException",
                    "User with ID " + userId + " does not have such Order with ID " + orderId,
                    "404 NOT_FOUND"
            );

            webTestClient.delete()
                    .uri("/orders?userId=" + userId + "&orderId=" + orderId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(IncorrectData.class)
                    .isEqualTo(expectedValue);
        }

    }

}
