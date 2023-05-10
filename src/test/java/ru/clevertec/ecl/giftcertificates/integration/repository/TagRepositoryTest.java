package ru.clevertec.ecl.giftcertificates.integration.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import ru.clevertec.ecl.giftcertificates.integration.BaseIntegrationTest;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.repository.TagRepository;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
class TagRepositoryTest extends BaseIntegrationTest {

    private final TagRepository tagRepository;

    @Nested
    class FindByNameInTest {

        @Test
        @DisplayName("test should return List with expected size")
        void testShouldReturnListWithExpectedSize() {
            int expectedSize = 2;

            List<Tag> actualValues =
                    tagRepository.findByNameIn(List.of("Pepsi", "Sprite"));

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            List<Tag> actualValues
                    = tagRepository.findByNameIn(List.of("Cool"));

            assertThat(actualValues).isEmpty();
        }

    }

    @Nested
    class DeleteByIdTest {

        @Test
        @DisplayName("test should throw DataIntegrityViolationException")
        void testShouldThrowDataIntegrityViolationException() {
            assertThrows(DataIntegrityViolationException.class, () -> tagRepository.deleteById(4L));
        }

        @Test
        @DisplayName("test should delete expected value")
        void testShouldDeleteExpectedValue() {
            Tag tag = TagTestBuilder.aTag().withId(4L).withName("Cola").build();

            tagRepository.deleteRelation(tag.getId());
            tagRepository.deleteById(tag.getId());

            Optional<Tag> actualValue = tagRepository.findById(tag.getId());

            assertThat(actualValue).isEmpty();
        }

    }

    @Nested
    class FindTheMostWidelyUsedWithTheHighestCostTest {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() {
            Tag expectedValue = TagTestBuilder.aTag().withId(3L).withName("Sprite").build();
            long userId = 1L;

            Optional<Tag> actualValue = tagRepository.findTheMostWidelyUsedWithTheHighestCost(userId);

            assertThat(actualValue).isEqualTo(Optional.of(expectedValue));
        }

        @Test
        @DisplayName("test should return empty Optional")
        void testShouldReturnEmptyOptional() {
            long userId = 47L;

            Optional<Tag> actualValue = tagRepository.findTheMostWidelyUsedWithTheHighestCost(userId);

            assertThat(actualValue).isEmpty();
        }

    }

}
