package ru.clevertec.ecl.giftcertificates.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.giftcertificates.integration.BaseIntegrationTest;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.repository.GiftCertificateRepository;
import ru.clevertec.ecl.giftcertificates.util.impl.GiftCertificateTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@RequiredArgsConstructor
class GiftCertificateRepositoryTest extends BaseIntegrationTest {

    private final GiftCertificateRepository giftCertificateRepository;

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() {
            GiftCertificate expectedValue = GiftCertificateTestBuilder.aGiftCertificate()
                    .withName("Rick")
                    .withDescription("Vabulabudabda")
                    .build();

            Optional<GiftCertificate> actualValue = giftCertificateRepository.findById(expectedValue.getId());

            actualValue.ifPresent(giftCertificate -> assertAll(
                    () -> assertThat(giftCertificate.getName()).isEqualTo(expectedValue.getName()),
                    () -> assertThat(giftCertificate.getDescription()).isEqualTo(expectedValue.getDescription())
            ));
        }

        @Test
        @DisplayName("test should return empty Optional")
        void testShouldReturnEmptyOptional() {
            Optional<GiftCertificate> actualValue = giftCertificateRepository.findById(98L);

            assertThat(actualValue).isEmpty();
        }

    }

    @Nested
    class FindAllWithTagsTest {

        @Test
        @DisplayName("test should return List with expected size")
        void testShouldReturnListWithExpectedSize() {
            int expectedSize = 1;

            List<GiftCertificate> actualValues =
                    giftCertificateRepository.findAllWithTags("Sprite", "Pechkin", Sort.by("id"));

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return all values")
        void testShouldReturnAllValues() {
            int expectedSize = 7;

            List<GiftCertificate> actualValues = giftCertificateRepository.findAllWithTags(null, null);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            List<GiftCertificate> actualValues
                    = giftCertificateRepository.findAllWithTags("Jam", Sort.by("name"));

            assertThat(actualValues).isEmpty();
        }

        @Test
        @DisplayName("test should fetch tags by part of name")
        void testShouldFetchTagsByPartOfName() {
            Tag expectedValue = TagTestBuilder.aTag().withId(3L).withName("Morty").build();

            List<GiftCertificate> actualValues
                    = giftCertificateRepository.findAllWithTags("Mor", Sort.by("name"));

            assertThat(actualValues.get(0).getTags()).contains(expectedValue);
        }

    }

    @Nested
    class FindAllByIdInTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() {
           String expectedFirstName = "Morty";
           String expectedSecondName = "Pechkin";

            List<GiftCertificate> actualValue = giftCertificateRepository.findAllByIdIn(List.of(2L, 3L));

            assertAll(
                    () -> assertThat(actualValue.get(0).getName()).isEqualTo(expectedFirstName),
                    () -> assertThat(actualValue.get(1).getName()).isEqualTo(expectedSecondName)
            );
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            List<GiftCertificate> actualValue = giftCertificateRepository.findAllByIdIn(List.of(100L));

            assertThat(actualValue).isEmpty();
        }

    }

}
