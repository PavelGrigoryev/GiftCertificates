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
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchGiftCertificateException;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;
import ru.clevertec.ecl.giftcertificates.util.impl.GiftCertificateTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.TagTestBuilder;

import java.math.BigDecimal;
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
class GiftCertificateServiceImplTest {

    @Spy
    private GiftCertificateService giftCertificateService;
    @Mock
    private GiftCertificateDao giftCertificateDao;
    private final GiftCertificateMapper giftCertificateMapper = Mappers.getMapper(GiftCertificateMapper.class);
    private static final GiftCertificateTestBuilder TEST_BUILDER = GiftCertificateTestBuilder.aGiftCertificate();
    @Captor
    private ArgumentCaptor<GiftCertificate> captor;

    @BeforeEach
    void setUp() {
        giftCertificateService = new GiftCertificateServiceImpl(giftCertificateDao);
    }

    @Nested
    class FindAllTest {

        @Test
        @DisplayName("test findAll should return List of size 1")
        void testFindAllShouldReturnListOfSizeOne() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            int expectedSize = 1;

            doReturn(List.of(mockedGiftCertificate))
                    .when(giftCertificateDao)
                    .findAll();

            List<GiftCertificateResponse> actualValues = giftCertificateService.findAll();
            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test findAll should return sorted by id List of GiftCertificateResponse")
        void testFindAllShouldReturnSortedByIdListOfGiftCertificateResponse() {
            GiftCertificate firstMock = TEST_BUILDER.build();
            GiftCertificate secondMock = TEST_BUILDER.withId(2L).build();
            GiftCertificate thirdMock = TEST_BUILDER.withId(3L).build();
            List<GiftCertificate> mockedGiftCertificates = List.of(secondMock, thirdMock, firstMock);
            List<GiftCertificateResponse> expectedValues = Stream.of(firstMock, secondMock, thirdMock)
                    .map(giftCertificateMapper::toResponse)
                    .toList();

            doReturn(mockedGiftCertificates)
                    .when(giftCertificateDao)
                    .findAll();

            List<GiftCertificateResponse> actualValues = giftCertificateService.findAll();

            assertThat(actualValues).isEqualTo(expectedValues);
        }

        @Test
        @DisplayName("test findAll should return empty List")
        void testFindAllShouldReturnEmptyList() {
            doReturn(Collections.emptyList())
                    .when(giftCertificateDao)
                    .findAll();

            List<GiftCertificateResponse> actualValues = giftCertificateService.findAll();

            assertThat(actualValues).isEmpty();
        }

    }

    @Nested
    class FindByIdTest {

        @Test
        @DisplayName("test findById throw NoSuchGiftCertificateException")
        void testFindByIdThrowNoSuchGiftCertificateException() {
            long id = 2L;

            doThrow(new NoSuchGiftCertificateException(""))
                    .when(giftCertificateDao)
                    .findById(id);

            assertThrows(NoSuchGiftCertificateException.class, () -> giftCertificateService.findById(id));
        }

        @Test
        @DisplayName("test findById throw NoSuchGiftCertificateException with expected message")
        void testFindByIdThrowNoSuchGiftCertificateExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "GiftCertificate with ID " + id + " does not exist";

            Exception exception = assertThrows(NoSuchGiftCertificateException.class,
                    () -> giftCertificateService.findById(id));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test testFindById should return expected GiftCertificateResponse")
        void testFindByIdShouldReturnExpectedTagDto() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            long id = mockedGiftCertificate.getId();
            GiftCertificateResponse expectedValue = giftCertificateMapper.toResponse(mockedGiftCertificate);

            doReturn(Optional.of(mockedGiftCertificate))
                    .when(giftCertificateDao)
                    .findById(id);

            GiftCertificateResponse actualValue = giftCertificateService.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class FindAllWithTagsTest {

        @Test
        @DisplayName("test findAllWithTags should return List of size 1")
        void testFindAllWithTagsShouldReturnListOfSizeOne() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            int expectedSize = 1;

            doReturn(List.of(mockedGiftCertificate))
                    .when(giftCertificateDao)
                    .findAll();

            List<GiftCertificateResponse> actualValues = giftCertificateService.findAll();
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
                    .when(giftCertificateDao)
                    .findAllWithTags(null, null, null, null);

            List<GiftCertificateResponse> actualValues = giftCertificateService
                    .findAllWithTags(null, null, null, null);

            assertThat(actualValues).containsAll(expectedValues);
        }

        @Test
        @DisplayName("test findAllWithTags should return empty List")
        void testFindAllWithTagsShouldReturnEmptyList() {
            doReturn(Collections.emptyList())
                    .when(giftCertificateDao)
                    .findAllWithTags("", "", "", "");

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
            doReturn(expectedValue)
                    .when(giftCertificateDao)
                    .save(expectedValue);

            giftCertificateService.save(request);

            verify(giftCertificateDao).save(captor.capture());

            GiftCertificate captorValue = captor.getValue();

            assertThat(captorValue).isEqualTo(expectedValue);
        }

    }

    @Nested
    class UpdateTest {

        @Test
        @DisplayName("test update should return updated GiftCertificateResponse")
        void testUpdateShouldReturnUpdatedGiftCertificateResponse() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            GiftCertificateRequest expectedValue = giftCertificateMapper.toRequest(mockedGiftCertificate);
            long id = mockedGiftCertificate.getId();

            doReturn(Optional.of(mockedGiftCertificate))
                    .when(giftCertificateDao)
                    .findById(id);

            mockedGiftCertificate.setName("Destruction");
            mockedGiftCertificate.setDescription("Oh No!!!");

            doReturn(mockedGiftCertificate)
                    .when(giftCertificateDao)
                    .update(mockedGiftCertificate);

            GiftCertificateResponse actualValue = giftCertificateService.update(expectedValue);

            assertThat(actualValue.getId()).isEqualTo(expectedValue.getId());
            assertThat(actualValue.getName()).isNotEqualTo(expectedValue.getName());
            assertThat(actualValue.getDescription()).isNotEqualTo(expectedValue.getDescription());
        }

        @Test
        @DisplayName("test update should return GiftCertificateResponse without update")
        void testUpdateShouldReturnGiftCertificateResponseWithoutUpdate() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            GiftCertificateRequest expectedValue = giftCertificateMapper.toRequest(mockedGiftCertificate);
            long id = mockedGiftCertificate.getId();

            doReturn(Optional.of(mockedGiftCertificate))
                    .when(giftCertificateDao)
                    .findById(id);

            doReturn(mockedGiftCertificate)
                    .when(giftCertificateDao)
                    .update(mockedGiftCertificate);

            GiftCertificateResponse actualValue = giftCertificateService.update(expectedValue);

            assertThat(actualValue.getId()).isEqualTo(expectedValue.getId());
            assertThat(actualValue.getName()).isEqualTo(expectedValue.getName());
            assertThat(actualValue.getTags()).containsAll(expectedValue.getTags());
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test delete should invoke method 1 time")
        void testDeleteShouldInvokeOneTime() {
            GiftCertificate mockedGiftCertificate = TEST_BUILDER.build();
            long id = mockedGiftCertificate.getId();

            doReturn(Optional.of(mockedGiftCertificate))
                    .when(giftCertificateDao)
                    .delete(id);

            giftCertificateService.delete(id);

            verify(giftCertificateDao, times(1))
                    .delete(id);
        }

        @Test
        @DisplayName("test delete throw NoSuchGiftCertificateException")
        void testDeleteThrowNoSuchGiftCertificateException() {
            long id = 2L;

            doThrow(new NoSuchGiftCertificateException(""))
                    .when(giftCertificateDao)
                    .delete(id);

            assertThrows(NoSuchGiftCertificateException.class, () -> giftCertificateService.delete(id));
        }

        @Test
        @DisplayName("test delete throw NoSuchGiftCertificateException with expected message")
        void testDeleteThrowNoSuchGiftCertificateExceptionWithExpectedMessage() {
            long id = 1L;
            String expectedMessage = "There is no GiftCertificate with ID " + id + " to delete";

            Exception exception = assertThrows(NoSuchGiftCertificateException.class,
                    () -> giftCertificateService.delete(id));
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
                        .withTags(List.of(TagTestBuilder.aTag().build(),
                                TagTestBuilder.aTag().withId(2L).withName("Ash").build()))
                        .build()));
    }

}
