package ru.clevertec.ecl.giftcertificates.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderResponse;
import ru.clevertec.ecl.giftcertificates.dto.order.UpdateYourOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.AlreadyHaveThisCertificateException;
import ru.clevertec.ecl.giftcertificates.exception.NoRelationBetweenOrderAndUserException;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapperImpl;
import ru.clevertec.ecl.giftcertificates.mapper.OrderMapper;
import ru.clevertec.ecl.giftcertificates.mapper.OrderMapperImpl;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapperImpl;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapper;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapperImpl;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Order;
import ru.clevertec.ecl.giftcertificates.model.User;
import ru.clevertec.ecl.giftcertificates.repository.OrderRepository;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;
import ru.clevertec.ecl.giftcertificates.service.OrderService;
import ru.clevertec.ecl.giftcertificates.service.UserService;
import ru.clevertec.ecl.giftcertificates.util.impl.GiftCertificateTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.OrderPageRequestTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.OrderTestBuilder;
import ru.clevertec.ecl.giftcertificates.util.impl.UserTestBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = {UserMapperImpl.class, OrderMapperImpl.class, GiftCertificateMapperImpl.class, TagMapperImpl.class})
@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserService userService;
    @Mock
    private GiftCertificateService giftCertificateService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    private static final OrderTestBuilder TEST_BUILDER = OrderTestBuilder.aOrder();

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, userService, giftCertificateService, orderMapper, userMapper);
    }

    @Nested
    class MakeAnOrderTest {

        @Test
        @DisplayName("test should return expected OrderResponse")
        void testShouldReturnExpectedOrderResponse() {
            Order order = TEST_BUILDER.build();
            OrderResponse expectedValue = orderMapper.toDto(order);
            User user = UserTestBuilder.aUser().build();
            UserDto userDto = userMapper.toDto(user);
            MakeAnOrderRequest request = new MakeAnOrderRequest(1L, List.of(1L, 2L));

            doReturn(userDto)
                    .when(userService)
                    .findById(request.userId());

            doReturn(order.getGiftCertificates())
                    .when(giftCertificateService)
                    .findAllByIdIn(request.giftIds());

            doReturn(order)
                    .when(orderRepository)
                    .save(any(Order.class));

            OrderResponse actualValue = orderService.makeAnOrder(request);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test should return expected sum of prices")
        void testShouldReturnExpectedSumOfPrices() {
            Order expectedValue = TEST_BUILDER.withPrice(BigDecimal.valueOf(40)).build();
            User user = UserTestBuilder.aUser().build();
            UserDto userDto = userMapper.toDto(user);
            MakeAnOrderRequest request = new MakeAnOrderRequest(1L, List.of(1L, 2L));
            List<GiftCertificate> giftCertificates = List.of(
                    GiftCertificateTestBuilder.aGiftCertificate().withId(3L).build(),
                    GiftCertificateTestBuilder.aGiftCertificate().withId(4L).build()
            );

            doReturn(userDto)
                    .when(userService)
                    .findById(request.userId());

            doReturn(giftCertificates)
                    .when(giftCertificateService)
                    .findAllByIdIn(request.giftIds());

            doReturn(expectedValue)
                    .when(orderRepository)
                    .save(any(Order.class));

            OrderResponse actualValue = orderService.makeAnOrder(request);

            assertThat(actualValue.price()).isEqualTo(expectedValue.getPrice());
        }

    }

    @Nested
    class FindAllByUserIdTest {

        @Test
        @DisplayName("test should return List of size 1")
        void testShouldReturnListOfSizeOne() {
            Order mockedOrder = TEST_BUILDER.build();
            OrderPageRequest request = OrderPageRequestTestBuilder.aOrderPageRequest().build();
            int expectedSize = 1;
            long userId = 1L;

            doReturn(List.of(mockedOrder))
                    .when(orderRepository)
                    .findAllByUserId(any(Long.class), any(PageRequest.class));

            List<OrderResponse> actualValues = orderService.findAllByUserId(userId, request);

            assertThat(actualValues).hasSize(expectedSize);
        }

        @Test
        @DisplayName("test should return List that contains expected value")
        void testShouldReturnListThatContainsExpectedValue() {
            Order mockedOrder = TEST_BUILDER.build();
            OrderPageRequest request = OrderPageRequestTestBuilder.aOrderPageRequest().build();
            long userId = 1L;

            doReturn(List.of(mockedOrder))
                    .when(orderRepository)
                    .findAllByUserId(any(Long.class), any(PageRequest.class));

            List<OrderResponse> actualValues = orderService.findAllByUserId(userId, request);

            assertThat(actualValues).contains(orderMapper.toDto(mockedOrder));
        }

        @Test
        @DisplayName("test should return empty List")
        void testShouldReturnEmptyList() {
            OrderPageRequest request = OrderPageRequestTestBuilder.aOrderPageRequest().build();
            long userId = 1L;

            doReturn(List.of())
                    .when(orderRepository)
                    .findAllByUserId(any(Long.class), any(PageRequest.class));

            List<OrderResponse> actualValues = orderService.findAllByUserId(userId, request);

            assertThat(actualValues).isEmpty();
        }

    }

    @Nested
    class AddToYourOrder {

        @Test
        @DisplayName("test should return expected value")
        void testShouldReturnExpectedValue() {
            Order mockedOrder = TEST_BUILDER.build();
            Order expectedValue = TEST_BUILDER.withPrice(BigDecimal.valueOf(40)).build();
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, mockedOrder.getId(), List.of(3L, 4L));

            doReturn(Optional.of(mockedOrder))
                    .when(orderRepository)
                    .findOrderByIdAndUserId(request.orderId(), request.userId());

            doReturn(mockedOrder.getGiftCertificates())
                    .when(giftCertificateService)
                    .findAllByIdIn(request.giftIds());

            doReturn(expectedValue)
                    .when(orderRepository)
                    .saveAndFlush(mockedOrder);

            OrderResponse actualValue = orderService.addToYourOrder(request);

            assertThat(actualValue).isEqualTo(orderMapper.toDto(expectedValue));
        }

        @Test
        @DisplayName("test should return expected sum of prices")
        void testShouldReturnExpectedSumOfPrices() {
            Order mockedOrder = TEST_BUILDER.build();
            Order expectedValue = TEST_BUILDER.withPrice(BigDecimal.valueOf(40)).build();
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, mockedOrder.getId(), List.of(3L, 4L));
            List<GiftCertificate> giftCertificates = List.of(
                    GiftCertificateTestBuilder.aGiftCertificate().withId(3L).build(),
                    GiftCertificateTestBuilder.aGiftCertificate().withId(4L).build()
            );

            doReturn(Optional.of(mockedOrder))
                    .when(orderRepository)
                    .findOrderByIdAndUserId(request.orderId(), request.userId());

            doReturn(giftCertificates)
                    .when(giftCertificateService)
                    .findAllByIdIn(request.giftIds());

            doReturn(expectedValue)
                    .when(orderRepository)
                    .saveAndFlush(mockedOrder);

            OrderResponse actualValue = orderService.addToYourOrder(request);

            assertThat(actualValue.price()).isEqualTo(expectedValue.getPrice());
        }

        @Test
        @DisplayName("test should throw NoRelationBetweenOrderAndUserException")
        void testShouldThrowNoRelationBetweenOrderAndUserException() {
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, 1L, List.of(3L, 4L));

            doThrow(new NoRelationBetweenOrderAndUserException(""))
                    .when(orderRepository)
                    .findOrderByIdAndUserId(request.orderId(), request.userId());

            assertThrows(NoRelationBetweenOrderAndUserException.class, () -> orderService.addToYourOrder(request));
        }

        @Test
        @DisplayName("test should throw NoRelationBetweenOrderAndUserException with expected message")
        void testShouldThrowNoRelationBetweenOrderAndUserExceptionWithExpectedMessage() {
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, 1L, List.of(3L, 4L));
            String expectedMessage = "User with ID " + request.userId()
                                     + " does not have such Order with ID " + request.orderId();

            Exception exception = assertThrows(NoRelationBetweenOrderAndUserException.class,
                    () -> orderService.addToYourOrder(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

        @Test
        @DisplayName("test should throw AlreadyHaveThisCertificateException")
        void testShouldThrowAlreadyHaveThisCertificateException() {
            Order mockedOrder = TEST_BUILDER.build();
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, mockedOrder.getId(), List.of(3L, 4L));

            doReturn(Optional.of(mockedOrder))
                    .when(orderRepository)
                    .findOrderByIdAndUserId(request.orderId(), request.userId());

            doReturn(mockedOrder.getGiftCertificates())
                    .when(giftCertificateService)
                    .findAllByIdIn(request.giftIds());

            doThrow(new DataIntegrityViolationException(""))
                    .when(orderRepository)
                    .saveAndFlush(mockedOrder);

            assertThrows(AlreadyHaveThisCertificateException.class, () -> orderService.addToYourOrder(request));
        }

        @Test
        @DisplayName("test should throw AlreadyHaveThisCertificateException with expected message")
        void testShouldThrowAlreadyHaveThisCertificateExceptionWithExpectedMessage() {
            Order mockedOrder = TEST_BUILDER.build();
            UpdateYourOrderRequest request = new UpdateYourOrderRequest(1L, mockedOrder.getId(), List.of(3L, 4L));
            String expectedMessage = "Your already have this GiftCertificate";

            doReturn(Optional.of(mockedOrder))
                    .when(orderRepository)
                    .findOrderByIdAndUserId(request.orderId(), request.userId());

            doReturn(mockedOrder.getGiftCertificates())
                    .when(giftCertificateService)
                    .findAllByIdIn(request.giftIds());

            doThrow(new DataIntegrityViolationException(""))
                    .when(orderRepository)
                    .saveAndFlush(mockedOrder);

            Exception exception = assertThrows(AlreadyHaveThisCertificateException.class,
                    () -> orderService.addToYourOrder(request));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

    @Nested
    class DeleteTest {

        @Test
        @DisplayName("test should invoke method 1 time")
        void testShouldInvokeOneTime() {
            Order mockedOrder = TEST_BUILDER.build();
            long orderId = mockedOrder.getId();
            long userId = 1L;

            doReturn(Optional.of(mockedOrder))
                    .when(orderRepository)
                    .findOrderByIdAndUserId(orderId, userId);

            doNothing()
                    .when(orderRepository)
                    .delete(mockedOrder);

            orderService.delete(userId, orderId);

            verify(orderRepository, times(1))
                    .delete(mockedOrder);
        }

        @Test
        @DisplayName("test throw NoRelationBetweenOrderAndUserException")
        void testThrowNoRelationBetweenOrderAndUserException() {
            long orderId = 2L;
            long userId = 1L;

            doThrow(new NoRelationBetweenOrderAndUserException(""))
                    .when(orderRepository)
                    .findOrderByIdAndUserId(orderId, userId);

            assertThrows(NoRelationBetweenOrderAndUserException.class, () -> orderService.delete(userId, orderId));
        }

        @Test
        @DisplayName("test throw NoRelationBetweenOrderAndUserException with expected message")
        void testThrowNoRelationBetweenOrderAndUserExceptionWithExpectedMessage() {
            long orderId = 2L;
            long userId = 1L;
            String expectedMessage = "User with ID " + userId + " does not have such Order with ID " + orderId;

            Exception exception = assertThrows(NoRelationBetweenOrderAndUserException.class,
                    () -> orderService.delete(userId, orderId));
            String actualMessage = exception.getMessage();

            assertThat(actualMessage).isEqualTo(expectedMessage);
        }

    }

}
