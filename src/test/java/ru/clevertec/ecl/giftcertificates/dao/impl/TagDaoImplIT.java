package ru.clevertec.ecl.giftcertificates.dao.impl;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.clevertec.ecl.giftcertificates.config.DatabaseConfigForIT;
import ru.clevertec.ecl.giftcertificates.dao.TagDao;
import ru.clevertec.ecl.giftcertificates.exception.CannotDeleteTagException;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringJUnitConfig(DatabaseConfigForIT.class)
class TagDaoImplIT {

    private TagDao tagDao;
    @Autowired
    private SessionFactory sessionFactory;
    private static final TagTestBuilder TEST_BUILDER = TagTestBuilder.aTag();

    @BeforeEach
    void setUp() {
        tagDao = new TagDaoImpl(sessionFactory);
    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test findAll should return List where name of second Tag = Fanta")
        void testFindAllShouldReturnExpectedList() {
            List<Tag> actualValues = tagDao.findAll();

            assertThat(actualValues.get(1).getName()).isEqualTo("Fanta");
        }

        @Test
        @DisplayName("test findAll should contains Tag with id 3 and name Sprite")
        void testFindAllShouldContainsExpectedValue() {
            Tag expectedValue = TEST_BUILDER.withId(3L).withName("Sprite").build();

            List<Tag> actualValues = tagDao.findAll();

            assertThat(actualValues).contains(expectedValue);
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test findById should return expected Tag")
        void testFindByIdShouldReturnExpectedTag() {
            Tag expectedValue = TEST_BUILDER.withId(2L).withName("Fanta").build();

            Optional<Tag> actualValue = tagDao.findById(expectedValue.getId());

            actualValue.ifPresent(tag -> assertThat(tag.getName()).isEqualTo(expectedValue.getName()));
        }

        @Test
        @DisplayName("test findById should return empty Optional if it did not find any Tag")
        void testFindByIdShouldReturnEmptyOptional() {
            Optional<Tag> actualValue = tagDao.findById(15L);

            assertThat(actualValue).isEmpty();
        }

    }

    @Nested
    class SaveTest {

        @Test
        @DisplayName("test save should return saved Tag")
        void testSaveShouldReturnSavedTag() {
            Tag expectedValue = new Tag();
            expectedValue.setName("Mirinda");

            Tag actualValue = tagDao.save(expectedValue);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdateTest {

        @Test
        @DisplayName("test update should return updated Tag if it exist by id")
        void testUpdateShouldReturnUpdatedTag() {
            long expectedId = 1L;
            String oldName = "Pepsi";

            Tag tag = TEST_BUILDER.withId(1L).withName("Pizza").build();

            Tag actualValue = tagDao.update(tag);

            assertThat(actualValue.getId()).isEqualTo(expectedId);
            assertThat(actualValue.getName()).isNotEqualTo(oldName);
        }

        @Test
        @DisplayName("test update should return saved new Tag if it not exists by id")
        void testUpdateShouldReturnSavedNewTag() {
            Tag expectedValue = TEST_BUILDER.withId(6L).withName("Pizza").build();

            Tag actualValue = tagDao.update(expectedValue);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test delete should return deleted Tag")
        void testDeleteShouldReturnDeletedTag() {
            Tag expectedValue = TEST_BUILDER.withId(5L).withName("7-Up").build();

            Optional<Tag> actualValue = tagDao.delete(5L);

            actualValue.ifPresent(tag -> assertThat(tag.getName()).isEqualTo(expectedValue.getName()));
        }

        @Test
        @DisplayName("test delete should return empty Optional if it did not find any Tag")
        void testDeleteShouldReturnEmptyOptional() {
            Optional<Tag> actualValue = tagDao.delete(15L);

            assertThat(actualValue).isEmpty();
        }

        @Test
        @DisplayName("test delete should throw CannotDeleteTagException")
        void testDeleteShouldThrowCannotDeleteTagException() {
            assertThrows(CannotDeleteTagException.class, () -> tagDao.delete(1L));
        }

        @Test
        @DisplayName("test delete should throw CannotDeleteTagException with expected message")
        void testDeleteShouldThrowCannotDeleteTagExceptionWithExpectedMessage() {
            String expectedMessage = "You cannot delete the Tag, first you need to delete" +
                                     " the GiftCertificate that contains this Tag";

            Exception exception = assertThrows(CannotDeleteTagException.class, () -> tagDao.delete(1L));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

}
