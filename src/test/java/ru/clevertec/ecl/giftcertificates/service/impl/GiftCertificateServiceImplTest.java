package ru.clevertec.ecl.giftcertificates.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
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
import org.springframework.data.domain.Sort;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.PriceDurationUpdateRequest;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchGiftCertificateException;
import ru.clevertec.ecl.giftcertificates.exception.NoTagWithTheSameNameException;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapperImpl;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapperImpl;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.repository.GiftCertificateRepository;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;
import ru.clevertec.ecl.giftcertificates.service.TagService;
import ru.clevertec.ecl.giftcertificates.util.impl.GiftCertificateTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.math.BigDecimal;
import java.util.Collections;
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

@SpringBootTest(classes = {GiftCertificateMapperImpl.class, TagMapperImpl.class})
@ExtendWith(MockitoExtension.class)
class GiftCertificateServiceImplTest {

    private GiftCertificateService giftCertificateService;
    @Mock
    private TagService tagService;
    @Mock
    private GiftCertificateRepository giftCertificateRepository;
    @Mock
    private EntityManager entityManager;
    @Autowired
    private GiftCertificateMapper giftCertificateMapper;
    private static final GiftCertificateTestBuilder TEST_BUILDER = GiftCertificateTestBuilder.aGiftCertificate();
    @Captor
    private ArgumentCaptor<GiftCertificate> captor;

    @BeforeEach
    void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateRepository, tagService,
                giftCertificateMapper, entityManager);
    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test throw NoSuchGiftCertificateException")
        void testThrowNoSuchGiftCertificateException() {
            long id = 2L;

            doThrow(new NoSuchGiftCertificateException(""))
                    .when(giftCertificateRepository)
                    .findById(id);

            assertThrows(NoSuchGiftCertificateException.class, () -> giftCertificateService.findById(id));
        }

        @Test
        @DisplayName("test throw NoSuchGiftCertificateException with expected message")
        void testThrowNoSuchGiftCertificateExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "GiftCertificate with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchGiftCertificateException.class,
                    () -> giftCertificateService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should return expected GiftCertificateResponse")
        void testShouldReturnExpectedTagDto() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            long id = mockedGiftCertificate.getId();
            GiftCertificateResponse expectedValue = giftCertificateMapper.toResponse(mockedGiftCertificate);

            doReturn(Optional.of(mockedGiftCertificate))
                    .when(giftCertificateRepository)
                    .findById(id);

            GiftCertificateResponse actualValue = giftCertificateService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllWithTagsTest {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            int expectedSize = 1;

            doReturn(List.of(mockedGiftCertificate))
                    .when(giftCertificateRepository)
                    .findAllWithTags("Pepsi", "Little", Sort.by("createDate").descending());

            List<GiftCertificateResponse> actualValues
                    = giftCertificateService.findAllWithTags("Pepsi", "Little", "date", "desc");
            assertThat(actualValues).hasSize(expectedSize);
        }


        @Test
        @DisplayName("test findAllWithTags should work like findAll")
        void testFindAllWithTagsShouldWorkLikeFindAll() {
            List<GiftCertificate> mockedGiftCertificates = List.of(
                    TEST_BUILDER.build(),
                    TEST_BUILDER.withId(2L).withName("John").build(),
                    TEST_BUILDER.withId(3L).withDescription("Sos").withPrice(BigDecimal.ONE).build()
            );
            List<GiftCertificateResponse> expectedValues = mockedGiftCertificates.stream()
                    .map(giftCertificateMapper::toResponse)
                    .toList();

            doReturn(mockedGiftCertificates)
                    .when(giftCertificateRepository)
                    .findAllWithTags(null, Sort.by("id").ascending());

            List<GiftCertificateResponse> actualValues = giftCertificateService
                    .findAllWithTags(null, null, null, null);

            assertThat(actualValues).containsAll(expectedValues);
        }

        @Test
        @DisplayName("test findAllWithTags should return empty List")
        void testFindAllWithTagsShouldReturnEmptyList() {
            doReturn(Collections.emptyList())
                    .when(giftCertificateRepository)
                    .findAllWithTags("", "", Sort.by("name").ascending());

            List<GiftCertificateResponse> actualValues = giftCertificateService
                    .findAllWithTags("", "", "", "");

            assertThat(actualValues).isEmpty();
        }

    }

    @Nested
    class SaveTest {

        @ParameterizedTest(name = "{arguments} test")
        @DisplayName("test save should capture saved value")
        @MethodSource("ru.clevertec.ecl.giftcertificates.service.impl.GiftCertificateServiceImplTest#getArgumentsForSaveTest")
        void testSaveShouldCaptureValue(GiftCertificate expectedValue) {
            GiftCertificateRequest request = giftCertificateMapper.toRequest(expectedValue);
            List<String> names = expectedValue.getTags().stream()
                    .map(Tag::getName)
                    .toList();

            doReturn(expectedValue.getTags())
                    .when(tagService)
                    .findByNameIn(names);

            doReturn(expectedValue)
                    .when(entityManager)
                    .merge(any(GiftCertificate.class));

            doReturn(expectedValue)
                    .when(giftCertificateRepository)
                    .save(expectedValue);

            giftCertificateService.save(request);
            verify(giftCertificateRepository).save(captor.capture());
            GiftCertificate captorValue = captor.getValue();

            assertThat(captorValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should throw NoTagWithTheSameNameException")
        void testShouldThrowNoTagWithTheSameNameException() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            GiftCertificateRequest mockedDto = giftCertificateMapper.toRequest(mockedGiftCertificate);

            doThrow(new IllegalStateException(""))
                    .when(entityManager)
                    .merge(any(GiftCertificate.class));

            assertThrows(NoTagWithTheSameNameException.class, () -> giftCertificateService.save(mockedDto));
        }

        @Test
        @DisplayName("test should throw NoTagWithTheSameNameException with expected message")
        void testShouldThrowNoTagWithTheSameNameExceptionWithExpectedMessage() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            GiftCertificateRequest mockedDto = giftCertificateMapper.toRequest(mockedGiftCertificate);
            String expectedMessage = "There should be no tags with the same name!";

            doThrow(new PersistenceException(""))
                    .when(entityManager)
                    .merge(any(GiftCertificate.class));

            Exception exception = assertThrows(NoTagWithTheSameNameException.class,
                    () -> giftCertificateService.save(mockedDto));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class UpdateTest {

        @Test
        @DisplayName("test should return updated GiftCertificateResponse")
        void testShouldReturnUpdatedGiftCertificateResponse() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            PriceDurationUpdateRequest expectedValue = new PriceDurationUpdateRequest(1L, BigDecimal.ONE, 7);
            long id = mockedGiftCertificate.getId();

            doReturn(Optional.of(mockedGiftCertificate))
                    .when(giftCertificateRepository)
                    .findById(id);

            mockedGiftCertificate.setPrice(expectedValue.price());
            mockedGiftCertificate.setDuration(expectedValue.duration());

            doReturn(mockedGiftCertificate)
                    .when(giftCertificateRepository)
                    .saveAndFlush(mockedGiftCertificate);

            GiftCertificateResponse actualValue = giftCertificateService.update(expectedValue);

            assertThat(actualValue.id()).isEqualTo(expectedValue.id());
            assertThat(actualValue.price()).isEqualTo(expectedValue.price());
            assertThat(actualValue.duration()).isEqualTo(expectedValue.duration());
        }

        @Test
        @DisplayName("test should return GiftCertificateResponse without update")
        void testShouldReturnGiftCertificateResponseWithoutUpdate() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            PriceDurationUpdateRequest expectedValue = new PriceDurationUpdateRequest(
                    mockedGiftCertificate.getId(),
                    mockedGiftCertificate.getPrice(),
                    mockedGiftCertificate.getDuration());
            long id = mockedGiftCertificate.getId();

            doReturn(Optional.of(mockedGiftCertificate))
                    .when(giftCertificateRepository)
                    .findById(id);

            doReturn(mockedGiftCertificate)
                    .when(giftCertificateRepository)
                    .saveAndFlush(mockedGiftCertificate);

            GiftCertificateResponse actualValue = giftCertificateService.update(expectedValue);

            assertThat(actualValue.id()).isEqualTo(expectedValue.id());
            assertThat(actualValue.price()).isEqualTo(expectedValue.price());
            assertThat(actualValue.duration()).isEqualTo(expectedValue.duration());
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test should invoke method 1 time")
        void testShouldInvokeOneTime() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            long id = mockedGiftCertificate.getId();

            doReturn(Optional.of(mockedGiftCertificate))
                    .when(giftCertificateRepository)
                    .findById(id);

            doNothing()
                    .when(giftCertificateRepository)
                    .delete(mockedGiftCertificate);

            giftCertificateService.delete(id);

            verify(giftCertificateRepository, times(1))
                    .delete(mockedGiftCertificate);
        }

        @Test
        @DisplayName("test throw NoSuchGiftCertificateException")
        void testThrowNoSuchGiftCertificateException() {
            long id = 2L;

            doThrow(new NoSuchGiftCertificateException(""))
                    .when(giftCertificateRepository)
                    .findById(id);

            assertThrows(NoSuchGiftCertificateException.class, () -> giftCertificateService.delete(id));
        }

        @Test
        @DisplayName("test throw NoSuchGiftCertificateException with expected message")
        void testThrowNoSuchGiftCertificateExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "There is no GiftCertificate with ID " + id + " to delete";

            Exception exception = assertThrows(NoSuchGiftCertificateException.class,
                    () -> giftCertificateService.delete(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class FindAllByIdIn {

        @Test
        @DisplayName("test should return List that contains expected values")
        void testShouldReturnListThatContainsExpectedValues() {
            GiftCertificate firstMockedTag = TEST_BUILDER.withId(3L).withName("Big").build();
            GiftCertificate secondMockedTag = TEST_BUILDER.withId(2L).withName("Little").build();
            List<Long> ids = List.of(3L, 2L);
            List<GiftCertificate> expectedValues = List.of(firstMockedTag, secondMockedTag);

            doReturn(expectedValues)
                    .when(giftCertificateRepository)
                    .findAllByIdIn(ids);

            List<GiftCertificate> actualValues = giftCertificateService.findAllByIdIn(ids);

            assertThat(actualValues).containsAll(expectedValues);
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            List<Long> ids = List.of(4L);

            doReturn(List.of())
                    .when(giftCertificateRepository)
                    .findAllByIdIn(ids);

            List<GiftCertificate> actualValues = giftCertificateService.findAllByIdIn(ids);

            assertThat(actualValues).isEmpty();
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
                        .withTags(List.of(TagTestBuilder.aTag().build(),
                                TagTestBuilder.aTag().withId(2L).withName("Ash").build()))
                        .build()));
    }

}
