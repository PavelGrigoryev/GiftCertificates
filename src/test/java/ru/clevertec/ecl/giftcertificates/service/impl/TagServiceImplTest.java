package ru.clevertec.ecl.giftcertificates.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.ecl.giftcertificates.dao.TagDao;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchTagException;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapper;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.service.TagService;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TagServiceImplTest {

    @Spy
    private TagService tagService;
    @Mock
    private TagDao tagDao;
    private final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);
    private static final TagTestBuilder testBuilder = TagTestBuilder.aTag();
    @Captor
    private ArgumentCaptor<Tag> captor;

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(tagDao);
    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test findAll should return List of size 1")
        void testFindAllShouldReturnListOfSizeOne() {
            Tag mockedTag = testBuilder.build();
            int expectedSize = 1;

            doReturn(List.of(mockedTag))
                    .when(tagDao)
                    .findAll();

            List<TagDto> actualValues = tagService.findAll();
            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test findAll should return sorted by id List of TagDto")
        void testFindAllShouldReturnSortedByIdListOfTagDto() {
            Tag firstMock = testBuilder.build();
            Tag secondMock = testBuilder.withId(2L).build();
            Tag thirdMock = testBuilder.withId(3L).build();
            List<Tag> mockedTags = List.of(secondMock, thirdMock, firstMock);
            List<TagDto> expectedValues = Stream.of(firstMock, secondMock, thirdMock)
                    .map(tagMapper::toDto)
                    .toList();

            doReturn(mockedTags)
                    .when(tagDao)
                    .findAll();

            List<TagDto> actualValues = tagService.findAll();

            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("test findAll should return empty List")
        void testFindAllShouldReturnEmptyList() {
            doReturn(Collections.emptyList())
                    .when(tagDao)
                    .findAll();

            List<TagDto> actualValues = tagService.findAll();

            assertThat(actualValues).isEmpty();
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test findById throw NoSuchTagException")
        void testFindByIdThrowNoSuchTagException() {
            long id = 2L;

            doThrow(new NoSuchTagException(""))
                    .when(tagDao)
                    .findById(id);

            assertThrows(NoSuchTagException.class, () -> tagService.findById(id));
        }

        @Test
        @DisplayName("test findById throw NoSuchTagException with expected message")
        void testFindByIdThrowNoSuchTagExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "Tag with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchTagException.class, () -> tagService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test testFindById should return expected TagDto")
        void testFindByIdShouldReturnExpectedTagDto() {
            Tag mockedTag = testBuilder.build();
            long id = mockedTag.getId();
            TagDto expectedValue = tagMapper.toDto(mockedTag);

            doReturn(Optional.of(mockedTag))
                    .when(tagDao)
                    .findById(id);

            TagDto actualValue = tagService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class SaveTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test save should capture saved value")
        @MethodSource("ru.clevertec.ecl.giftcertificates.service.impl.TagServiceImplTest#getArgumentsForSaveTest")
        void testSaveShouldCaptureValue(Tag expectedValue) {
            doReturn(expectedValue)
                    .when(tagDao)
                    .save(expectedValue);

            tagService.save(tagMapper.toDto(expectedValue));

            verify(tagDao).save(captor.capture());

            Tag captorValue = captor.getValue();

            assertThat(captorValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdateTest {

        @Test
        @DisplayName("test update should return updated TagDto")
        void testUpdateShouldReturnUpdatedTagDto() {
           Tag mockedTag = testBuilder.build();
           TagDto expectedValue = tagMapper.toDto(mockedTag);
           long id = mockedTag.getId();

           doReturn(Optional.of(mockedTag))
                   .when(tagDao)
                   .findById(id);

           mockedTag.setName("George");

           doReturn(mockedTag)
                   .when(tagDao)
                   .update(mockedTag);

            TagDto actualValue = tagService.update(expectedValue);

            assertThat(actualValue.getId()).isEqualTo(expectedValue.getId());
            assertThat(actualValue.getName()).isNotEqualTo(expectedValue.getName());
        }

        @Test
        @DisplayName("test update should return TagDto without update")
        void testUpdateShouldReturnTagDtoWithoutUpdate() {
            Tag mockedTag = testBuilder.build();
            TagDto expectedValue = tagMapper.toDto(mockedTag);
            long id = mockedTag.getId();

            doReturn(Optional.of(mockedTag))
                    .when(tagDao)
                    .findById(id);

            TagDto actualValue = tagService.update(expectedValue);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test delete should invoke method 1 time")
        void testDeleteShouldInvokeOneTime() {
            Tag mockedTag = testBuilder.build();
            long id = mockedTag.getId();

            doReturn(Optional.of(mockedTag))
                    .when(tagDao)
                    .delete(id);

            tagService.delete(id);

            verify(tagDao, times(1))
                    .delete(id);
        }

        @Test
        @DisplayName("test delete throw NoSuchTagException")
        void testDeleteThrowNoSuchTagException() {
            long id = 2L;

            doThrow(new NoSuchTagException(""))
                    .when(tagDao)
                    .delete(id);

            assertThrows(NoSuchTagException.class, () -> tagService.delete(id));
        }

        @Test
        @DisplayName("test delete throw NoSuchTagException with expected message")
        void testDeleteThrowNoSuchTagExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "There is no Tag with ID " + id + " to delete";

            Exception exception = assertThrows(NoSuchTagException.class, () -> tagService.delete(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    private static Stream<Arguments> getArgumentsForSaveTest() {
        return Stream.of(
                Arguments.of(testBuilder.build()),
                Arguments.of(testBuilder.withId(2L)
                        .withName("Scorpion")
                        .build()),
                Arguments.of(testBuilder.withId(3L)
                        .withName("Sub-zero")
                        .build()));
    }

}
