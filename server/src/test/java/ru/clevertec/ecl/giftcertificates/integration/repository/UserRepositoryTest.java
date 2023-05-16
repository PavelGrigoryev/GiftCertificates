package ru.clevertec.ecl.giftcertificates.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.giftcertificates.integration.BaseIntegrationTest;
import ru.clevertec.ecl.giftcertificates.model.User;
import ru.clevertec.ecl.giftcertificates.repository.UserRepository;
import ru.clevertec.ecl.giftcertificates.util.impl.UserTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class UserRepositoryTest extends BaseIntegrationTest {

    private final UserRepository userRepository;

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() {
            User expectedValue = UserTestBuilder.aUser().withUsername("Bully").withOrders(List.of()).build();

            Optional<User> actualValue = userRepository.findById(expectedValue.getId());

            assertThat(actualValue).isEqualTo(Optional.of(expectedValue));
        }

        @Test
        @DisplayName("test should return empty Optional")
        void testShouldReturnEmptyOptional() {
            Optional<User> actualValue = userRepository.findById(81L);

            assertThat(actualValue).isEmpty();
        }

    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return Page with expected size")
        void testShouldReturnPageWithExpectedSize() {
            PageRequest request = PageRequest.of(0, 5);
            int expectedSize = 5;

            Page<User> actualValues = userRepository.findAll(request);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return sorted Page by username")
        void testShouldReturnSortedPageByUsername() {
            PageRequest request = PageRequest.of(0, 5, Sort.by("username"));

            Page<User> actualValues = userRepository.findAll(request);

            assertThat(actualValues.getContent().get(0).getUsername()).isEqualTo("Arnold");
        }

        @Test
        @DisplayName("test should return empty Page")
        void testShouldReturnEmptyPage() {
            PageRequest request = PageRequest.of(1, 5);

            Page<User> actualValues = userRepository.findAll(request);

            assertThat(actualValues).isEmpty();
        }

    }

}
