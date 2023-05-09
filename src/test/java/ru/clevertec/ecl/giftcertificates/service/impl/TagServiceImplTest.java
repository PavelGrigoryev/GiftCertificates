package ru.clevertec.ecl.giftcertificates.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.TagPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchTagException;
import ru.clevertec.ecl.giftcertificates.exception.NoTagWithTheSameNameException;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapper;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.repository.TagRepository;
import ru.clevertec.ecl.giftcertificates.util.impl.TagPageRequestTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    private TagServiceImpl tagService;
    @Mock
    private TagRepository tagRepository;
    @Autowired
    private TagMapper tagMapper;
    private static final TagTestBuilder TEST_BUILDER = TagTestBuilder.aTag();
    @Captor
    private ArgumentCaptor<Tag> captor;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagRepository, tagMapper);
    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test should throw NoSuchTagException")
        void testShouldThrowNoSuchTagException() {
            long id = 2L;

            doThrow(new NoSuchTagException(""))
                    .when(tagRepository)
                    .findById(id);

            assertThrows(NoSuchTagException.class, () -> tagService.findById(id));
        }

        @Test
        @DisplayName("test should throw NoSuchTagException with expected message")
        void testShouldThrowNoSuchTagExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "Tag with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchTagException.class, () -> tagService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected TagDto")
        void testShouldReturnExpectedTagDto() {
            Tag mockedTag = TEST_BUILDER.build();
            long id = mockedTag.getId();
            TagDto expectedValue = tagMapper.toDto(mockedTag);

            doReturn(Optional.of(mockedTag))
                    .when(tagRepository)
                    .findById(id);

            TagDto actualValue = tagService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            Tag mockedTag = TEST_BUILDER.build();
            TagPageRequest request = TagPageRequestTestBuilder.aTagPageRequest().build();
            int expectedSize = 1;
            Page<Tag> page = new PageImpl<>(List.of(mockedTag));

            doReturn(page)
                    .when(tagRepository)
                    .findAll(any(PageRequest.class));

            List<TagDto> actualValues = tagService.findAll(request);
            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value")
        void testShouldReturnListThatContainsExpectedValue() {
            Tag mockedTag = TEST_BUILDER.build();
            TagPageRequest request = TagPageRequestTestBuilder.aTagPageRequest().build();
            Page<Tag> page = new PageImpl<>(List.of(mockedTag));

            doReturn(page)
                    .when(tagRepository)
                    .findAll(any(PageRequest.class));

            List<TagDto> actualValues = tagService.findAll(request);
            assertThat(actualValues).contains(tagMapper.toDto(mockedTag));
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            TagPageRequest request = TagPageRequestTestBuilder.aTagPageRequest().build();

            doReturn(Page.empty())
                    .when(tagRepository)
                    .findAll(any(PageRequest.class));

            List<TagDto> actualValues = tagService.findAll(request);

            assertThat(actualValues).isEmpty();
        }

    }

    @Nested
    class SaveTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test should capture saved value")
        @MethodSource("ru.clevertec.ecl.giftcertificates.service.impl.TagServiceImplTest#getArgumentsForSaveTest")
        void testShouldCaptureValue(Tag expectedValue) {
            TagDto dto = tagMapper.toDto(expectedValue);

            doReturn(expectedValue)
                    .when(tagRepository)
                    .save(expectedValue);

            tagService.save(dto);

            verify(tagRepository).save(captor.capture());

            Tag captorValue = captor.getValue();

            assertThat(captorValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw NoTagWithTheSameNameException")
        void testShouldThrowNoTagWithTheSameNameException() {
            Tag mockedTag = TEST_BUILDER.build();
            TagDto mockedDto = tagMapper.toDto(mockedTag);

            doThrow(new DataIntegrityViolationException(""))
                    .when(tagRepository)
                    .save(mockedTag);

            assertThrows(NoTagWithTheSameNameException.class, () -> tagService.save(mockedDto));
        }

        @Test
        @DisplayName("test should throw NoTagWithTheSameNameException with expected message")
        void testShouldThrowNoTagWithTheSameNameExceptionWithExpectedMessage() {
            Tag mockedTag = TEST_BUILDER.build();
            TagDto mockedDto = tagMapper.toDto(mockedTag);
            String expectedMessage = "Tag name " + mockedTag.getName() + " is already exist! It must be unique!";

            doThrow(new DataIntegrityViolationException(""))
                    .when(tagRepository)
                    .save(mockedTag);

            Exception exception = assertThrows(NoTagWithTheSameNameException.class, () -> tagService.save(mockedDto));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class UpdateTest {

        @Test
        @DisplayName("test should return updated TagDto")
        void testShouldReturnUpdatedTagDto() {
            Tag mockedTag = TEST_BUILDER.build();
            TagDto expectedValue = tagMapper.toDto(mockedTag);
            long id = mockedTag.getId();

            doReturn(Optional.of(mockedTag))
                    .when(tagRepository)
                    .findById(id);

            mockedTag.setName("George");

            doReturn(mockedTag)
                    .when(tagRepository)
                    .saveAndFlush(mockedTag);

            TagDto actualValue = tagService.update(expectedValue);

            assertThat(actualValue.id()).isEqualTo(expectedValue.id());
            assertThat(actualValue.name()).isNotEqualTo(expectedValue.name());
        }

        @Test
        @DisplayName("test should return TagDto without update")
        void testShouldReturnTagDtoWithoutUpdate() {
            Tag mockedTag = TEST_BUILDER.build();
            TagDto expectedValue = tagMapper.toDto(mockedTag);
            long id = mockedTag.getId();

            doReturn(Optional.of(mockedTag))
                    .when(tagRepository)
                    .findById(id);

            TagDto actualValue = tagService.update(expectedValue);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test should invoke method 1 time")
        void testShouldInvokeOneTime() {
            Tag mockedTag = TEST_BUILDER.build();
            long id = mockedTag.getId();

            doReturn(Optional.of(mockedTag))
                    .when(tagRepository)
                    .findById(id);

            doNothing()
                    .when(tagRepository)
                    .deleteById(id);

            tagService.delete(id);

            verify(tagRepository, times(1))
                    .deleteById(id);
        }

        @Test
        @DisplayName("test throw NoSuchTagException")
        void testThrowNoSuchTagException() {
            long id = 2L;

            doThrow(new NoSuchTagException(""))
                    .when(tagRepository)
                    .findById(id);

            assertThrows(NoSuchTagException.class, () -> tagService.delete(id));
        }

        @Test
        @DisplayName("test throw NoSuchTagException with expected message")
        void testThrowNoSuchTagExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "There is no Tag with ID " + id + " to delete";

            Exception exception = assertThrows(NoSuchTagException.class, () -> tagService.delete(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class FindByNameInTest {

        @Test
        @DisplayName("test should return List that contains expected values")
        void testShouldReturnListThatContainsExpectedValues() {
            Tag firstMockedTag = TEST_BUILDER.withName("Sara").build();
            Tag secondMockedTag = TEST_BUILDER.withId(2L).withName("John").build();
            List<String> names = List.of("Sara", "John");
            List<Tag> expectedValues = List.of(firstMockedTag, secondMockedTag);

            doReturn(expectedValues)
                    .when(tagRepository)
                    .findByNameIn(names);

            List<Tag> actualValues = tagService.findByNameIn(names);

            assertThat(actualValues).containsAll(expectedValues);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            List<String> names = List.of("Jimmy");

            doReturn(List.of())
                    .when(tagRepository)
                    .findByNameIn(names);

            List<Tag> actualValues = tagService.findByNameIn(names);

            assertThat(actualValues).isEmpty();
        }

    }

    @Nested
    class FindTheMostWidelyUsedWithTheHighestCostTest {

        @Test
        @DisplayName("test should return expected TagDto")
        void testShouldReturnExpectedTagDto() {
            Tag mockedTag = TEST_BUILDER.build();
            TagDto expectedValue = tagMapper.toDto(mockedTag);
            long userId = 2L;

            doReturn(Optional.of(mockedTag))
                    .when(tagRepository)
                    .findTheMostWidelyUsedWithTheHighestCost(userId);

            TagDto actualValue = tagService.findTheMostWidelyUsedWithTheHighestCost(userId);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw NoSuchTagException")
        void testShouldThrowNoSuchTagException() {
            long userId = 2L;

            doThrow(new NoSuchTagException(""))
                    .when(tagRepository)
                    .findTheMostWidelyUsedWithTheHighestCost(userId);

            assertThrows(NoSuchTagException.class, () -> tagService.findTheMostWidelyUsedWithTheHighestCost(userId));
        }

        @Test
        @DisplayName("test should throw NoSuchTagException with expected message")
        void testShouldThrowNoSuchTagExceptionWithExpectedMessage() {
            long userId = 1L;
            String expectedMessage = "There is no Tags in database with User ID " + userId + " connection";

            Exception exception = assertThrows(NoSuchTagException.class,
                    () -> tagService.findTheMostWidelyUsedWithTheHighestCost(userId));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    private static Stream<Arguments> getArgumentsForSaveTest() {
        return Stream.of(
                Arguments.of(TEST_BUILDER.build()),
                Arguments.of(TEST_BUILDER.withId(2L)
                        .withName("Scorpion")
                        .build()),
                Arguments.of(TEST_BUILDER.withId(3L)
                        .withName("Sub-zero")
                        .build()));
    }

}
