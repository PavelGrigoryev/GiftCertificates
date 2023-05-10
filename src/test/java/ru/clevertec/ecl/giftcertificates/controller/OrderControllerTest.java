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
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderResponse;
import ru.clevertec.ecl.giftcertificates.dto.order.UpdateYourOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.model.ValidationErrorResponse;
import ru.clevertec.ecl.giftcertificates.exception.model.Violation;
import ru.clevertec.ecl.giftcertificates.mapper.OrderMapper;
import ru.clevertec.ecl.giftcertificates.service.OrderService;
import ru.clevertec.ecl.giftcertificates.util.impl.OrderPageRequestTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.OrderTestBuilder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ControllerTest
@RequiredArgsConstructor
class OrderControllerTest {

    @MockBean
    private OrderService orderService;
    private final WebTestClient webTestClient;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;
    private static final OrderTestBuilder TEST_BUILDER = OrderTestBuilder.aOrder();

    @Nested
    class MakeAnOrderPostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() {
            OrderResponse response = orderMapper.toDto(TEST_BUILDER.build());
            MakeAnOrderRequest request = new MakeAnOrderRequest(1L, List.of(2L));

            doReturn(response)
                    .when(orderService)
                    .makeAnOrder(request);

            webTestClient.post()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(OrderResponse.class)
                    .isEqualTo(response);
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() {
            MakeAnOrderRequest request = new MakeAnOrderRequest(0L, List.of(2L));
            ValidationErrorResponse response = new ValidationErrorResponse(
                    "409 CONFLICT",
                    List.of(new Violation("userId", "User ID must be greater than 0"))
            );

            webTestClient.post()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                    .expectBody(ValidationErrorResponse.class)
                    .isEqualTo(response);
        }

    }

    @Nested
    class FindAllYourOrdersGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200")
        void testShouldReturnEmptyJsonAndStatus200() {
            long id = 2L;
            OrderPageRequest request = OrderPageRequestTestBuilder.aOrderPageRequest().build();

            doReturn(List.of())
                    .when(orderService)
                    .findAllByUserId(id, request);

            webTestClient.get()
                    .uri("/orders/2?page=0&size=10&sortBy=price")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody()
                    .json("[]");
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws JsonProcessingException {
            OrderResponse response = orderMapper.toDto(TEST_BUILDER.build());
            String jsonArray = objectMapper.writeValueAsString(List.of(response));

            doReturn(List.of(response))
                    .when(orderService)
                    .findAllByUserId(any(Long.class), any(OrderPageRequest.class));

            webTestClient.get()
                    .uri("/orders/2?page=0&size=10&sortBy=price")
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
                    List.of(new Violation("size", "Size must be greater than or equal 1"))
            );

            webTestClient.get()
                    .uri("/orders/2?page=0&size=-10&sortBy=price")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                    .expectBody(ValidationErrorResponse.class)
                    .isEqualTo(response);
        }

    }

    @Nested
    class AddToYourOrderPutEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            OrderResponse response = orderMapper.toDto(TEST_BUILDER.build());
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, 2L, List.of(3L));

            doReturn(response)
                    .when(orderService)
                    .addToYourOrder(request);

            webTestClient.put()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(OrderResponse.class)
                    .isEqualTo(response);
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() {
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, 2L, List.of(0L));
            ValidationErrorResponse response = new ValidationErrorResponse(
                    "409 CONFLICT",
                    List.of(new Violation("giftIds[0]", "GiftCertificate ID must be greater than 0"))
            );

            webTestClient.put()
                    .uri("/orders")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .exchange()
                    .expectStatus().isEqualTo(HttpStatus.CONFLICT)
                    .expectBody(ValidationErrorResponse.class)
                    .isEqualTo(response);
        }

    }

    @Nested
    class DeleteYourOrderDeleteEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() {
            long orderId = 2L;
            long userId = 1L;
            DeleteResponse response = new DeleteResponse("Order with ID " + orderId
                                                         + " for User with ID " + userId + " was successfully deleted");

            doNothing()
                    .when(orderService)
                    .delete(userId, orderId);

            webTestClient.delete()
                    .uri("/orders?userId=" + userId + "&orderId=" + orderId)
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(DeleteResponse.class)
                    .isEqualTo(response);
        }

    }

}
