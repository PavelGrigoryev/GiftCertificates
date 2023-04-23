package ru.clevertec.ecl.giftcertificates.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.ecl.giftcertificates.config.DatabaseConfigForIT;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.util.impl.GiftCertificateTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringJUnitConfig(DatabaseConfigForIT.class)
public class GiftCertificateDaoImplIT {

    private GiftCertificateDao giftCertificateDao;
    @Autowired
    private SessionFactory sessionFactory;
    private final static GiftCertificateTestBuilder TEST_BUILDER = GiftCertificateTestBuilder.aGiftCertificate();

    @BeforeEach
    void setUp() {
        giftCertificateDao = new GiftCertificateDaoImpl(sessionFactory);
    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test findAll should return List where name of second GiftCertificate is Morty")
        void testFindAllShouldReturnExpectedList() {
            List<GiftCertificate> actualValues = giftCertificateDao.findAll();

            assertThat(actualValues.get(1).getName()).isEqualTo("Morty");
        }

        @Test
        @DisplayName("test findAll should contains GiftCertificate with id 3 and description Hi")
        void testFindAllShouldContainsExpectedValue() {
            GiftCertificate expectedValue = TEST_BUILDER.withId(3L).withDescription("Hi").build();

            List<GiftCertificate> actualValues = giftCertificateDao.findAll();

            assertThat(actualValues).contains(expectedValue);
        }

        @Test
        @DisplayName("test findAll should have GiftCertificate with expected Tag")
        void testFindAllShouldHaveGiftCertificateWithExpectedTag() {
            Tag expectedValue = new Tag(1L, "Pepsi");

            List<GiftCertificate> actualValues = giftCertificateDao.findAll();

            assertThat(actualValues.get(0).getTags().get(0)).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test findById should return expected GiftCertificate")
        void testFindByIdShouldReturnExpectedGiftCertificate() {
            GiftCertificate expectedValue = TEST_BUILDER.withId(3L).withName("Pechkin").build();

            Optional<GiftCertificate> actualValue = giftCertificateDao.findById(expectedValue.getId());

            actualValue.ifPresent(giftCertificate -> assertThat(giftCertificate.getName())
                    .isEqualTo(expectedValue.getName()));
        }

        @Test
        @DisplayName("test findById should return empty Optional if it did not find any GiftCertificate")
        void testFindByIdShouldReturnEmptyOptional() {
            Optional<GiftCertificate> actualValue = giftCertificateDao.findById(15L);

            assertThat(actualValue).isEmpty();
        }

    }

    @Nested
    class FindAllWithTags {

        @Test
        @DisplayName("test findAllWithTags should return values by tagName")
        void testFindAllWithTagsShouldReturnValuesByTagName() {
            Tag expectedValue = TagTestBuilder.aTag().withId(3L).withName("Sprite").build();

            List<GiftCertificate> actualValues
                    = giftCertificateDao.findAllWithTags("Sprite", null, null, null);

            assertThat(actualValues.get(0).getTags()).contains(expectedValue);
        }

        @Test
        @DisplayName("test findAllWithTags should return values by part of description")
        void testFindAllWithTagsShouldReturnValuesByPartOfDescription() {
            List<GiftCertificate> actualValues
                    = giftCertificateDao.findAllWithTags(null, "bda", null, null);

            assertThat(actualValues.get(0).getDescription()).endsWith("bda");
            assertThat(actualValues.get(1).getDescription()).endsWith("bda");
        }

        @Test
        @DisplayName("test findAllWithTags should sort by name")
        void testFindAllWithTagsShouldSortByName() {
            List<GiftCertificate> actualValues
                    = giftCertificateDao.findAllWithTags(null, null, "name", null);

            assertThat(actualValues.get(0).getName()).isEqualTo("Azanti");
        }

    }

    @Nested
    class SaveTest {

        @Test
        @DisplayName("test save should return saved GiftCertificate")
        void testSaveShouldReturnSavedGiftCertificate() {
            List<Tag> tags = new ArrayList<>();
            tags.add(Tag.builder().name("Zero").build());
            tags.add(Tag.builder().name("Onixia").build());
            GiftCertificate expectedValue = GiftCertificate.builder()
                    .name("Holmes")
                    .description("Description")
                    .price(BigDecimal.ONE)
                    .duration(34)
                    .createDate(LocalDateTime.now())
                    .lastUpdateDate(LocalDateTime.now())
                    .tags(tags)
                    .build();

            GiftCertificate actualValue = giftCertificateDao.save(expectedValue);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdateTest {

        @Test
        @DisplayName("test update should return updated GiftCertificate if it exist by id")
        void testUpdateShouldReturnUpdatedGiftCertificate() {
            long expectedId = 5;
            String oldDescription = "Vabulabudabda";

            GiftCertificate giftCertificate = TEST_BUILDER.withId(5L).withDescription("Bang Bang")
                    .withTags(List.of(TagTestBuilder.aTag().withId(4L).build()))
                    .build();

            GiftCertificate actualValue = giftCertificateDao.update(giftCertificate);

            assertThat(actualValue.getId()).isEqualTo(expectedId);
            assertThat(actualValue.getName()).isNotEqualTo(oldDescription);
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test delete should return deleted GiftCertificate")
        void testDeleteShouldReturnDeletedGiftCertificate() {
            String expectedName = "Fiorello";

            Optional<GiftCertificate> actualValue = giftCertificateDao.delete(4L);

            actualValue.ifPresent(giftCertificate -> assertThat(giftCertificate.getName()).isEqualTo(expectedName));
        }

        @Test
        @DisplayName("test delete should return empty Optional if it did not find any GiftCertificate")
        void testDeleteShouldReturnEmptyOptional() {
            Optional<GiftCertificate> actualValue = giftCertificateDao.delete(15L);

            assertThat(actualValue).isEmpty();
        }

    }

}
