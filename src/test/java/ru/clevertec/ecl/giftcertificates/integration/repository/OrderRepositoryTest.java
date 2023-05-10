package ru.clevertec.ecl.giftcertificates.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.giftcertificates.integration.BaseIntegrationTest;
import ru.clevertec.ecl.giftcertificates.model.Order;
import ru.clevertec.ecl.giftcertificates.repository.OrderRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class OrderRepositoryTest extends BaseIntegrationTest {

    private final OrderRepository orderRepository;

    @Nested
    class FindAllByUserIdTest {

        @Test
        @DisplayName("test should return List with expected size")
        void testShouldReturnListWithExpectedSize() {
            int expectedSize = 3;
            PageRequest request = PageRequest.of(0, 5);
            long userId = 1;

            List<Order> actualValues = orderRepository.findAllByUserId(userId ,request);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return sorted List by price")
        void testShouldReturnSortedListByPrice() {
            BigDecimal expectedPrice = BigDecimal.valueOf(45);
            PageRequest request = PageRequest.of(0, 5, Sort.by("price"));
            long userId = 1;

            List<Order> actualValues = orderRepository.findAllByUserId(userId ,request);

            assertThat(actualValues.get(0).getPrice()).isEqualTo(expectedPrice);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            PageRequest request = PageRequest.of(0, 5);
            long userId = 89;

            List<Order> actualValues = orderRepository.findAllByUserId(userId ,request);

            assertThat(actualValues).isEmpty();
        }

    }

    @Nested
    class FindOrderByIdAndUserIdTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() {
            long orderId = 1L;
            long userId = 1L;

            Optional<Order> actualValue = orderRepository.findOrderByIdAndUserId(orderId, userId);

            actualValue.ifPresent(order -> assertThat(order.getId()).isEqualTo(orderId));
        }

        @Test
        @DisplayName("test should return empty Optional")
        void testShouldReturnEmptyOptional() {
            long orderId = 1L;
            long userId = 3L;

            Optional<Order> actualValue = orderRepository.findOrderByIdAndUserId(orderId, userId);

            assertThat(actualValue).isEmpty();
        }

    }

}
