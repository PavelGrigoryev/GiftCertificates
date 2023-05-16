package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderResponse;
import ru.clevertec.ecl.giftcertificates.dto.order.UpdateYourOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.AlreadyHaveThisCertificateException;
import ru.clevertec.ecl.giftcertificates.exception.NoRelationBetweenOrderAndUserException;
import ru.clevertec.ecl.giftcertificates.mapper.OrderMapper;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapper;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Order;
import ru.clevertec.ecl.giftcertificates.model.User;
import ru.clevertec.ecl.giftcertificates.repository.OrderRepository;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;
import ru.clevertec.ecl.giftcertificates.service.OrderService;
import ru.clevertec.ecl.giftcertificates.service.UserService;

import java.math.BigDecimal;
import java.util.List;

/**
 * The OrderServiceImpl class implements OrderService interface and provides the implementation for CRUD operations on
 * the {@link Order} entity and also adds new functionality, such as convert to dto {@link OrderResponse} from entity
 * and sort it by pageable with {@link PageRequest}. It uses a {@link OrderRepository} to interact with the database
 * and {@link OrderMapper} to map entity to dto and from dto to entity. For adding order with gift certificates to user
 * it uses {@link UserService}, {@link GiftCertificateService}. For manage transactions it uses annotation
 * {@link Transactional}.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    /**
     * Connects the {@link User} with the {@link Order} and with {@link GiftCertificate}.
     *
     * @param request the {@link MakeAnOrderRequest} with user ID and with list of gift certificates IDs.
     * @return {@link OrderResponse} with all parameters and mapped from Order entity.
     */
    @Override
    @Transactional
    public OrderResponse makeAnOrder(MakeAnOrderRequest request) {
        User user = userMapper.fromDto(userService.findById(request.userId()));
        List<GiftCertificate> giftCertificates = giftCertificateService.findAllByIdIn(request.giftIds());
        BigDecimal sum = getSum(giftCertificates);
        Order order = orderMapper.createOrder(user, giftCertificates, sum);
        OrderResponse response = orderMapper.toDto(orderRepository.save(order));
        log.info("makeAnOrder {}", response);
        return response;
    }

    /**
     * Finds all {@link OrderResponse} by {@link User} ID with pagination.
     *
     * @param id      the ID of the User.
     * @param request the {@link OrderPageRequest}. Orders will be sorted by its parameters and divided into pages.
     * @return a sorted by pageable and mapped from entity to dto list of all OrderResponses.
     */
    @Override
    public List<OrderResponse> findAllUserOrders(Long id, OrderPageRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.getSortBy()));
        List<OrderResponse> orders = orderRepository.findAllByUserId(id, pageRequest).stream()
                .map(orderMapper::toDto)
                .toList();
        log.info("findAllByUserId {} Orders size", orders.size());
        return orders;
    }

    /**
     * Updates {@link Order} by adding several {@link GiftCertificate}.
     *
     * @param request the {@link UpdateYourOrderRequest}  with user ID, order ID and with list of gift certificates IDs.
     * @return updated {@link OrderResponse}.
     * @throws NoRelationBetweenOrderAndUserException if {@link User} has no relations with Order.
     * @throws AlreadyHaveThisCertificateException    if Order already contains this GiftCertificate.
     */
    @Override
    @Transactional
    public OrderResponse updateUserOrder(UpdateYourOrderRequest request) {
        Order order = orderRepository.findOrderByIdAndUserId(request.orderId(), request.userId())
                .orElseThrow(() -> new NoRelationBetweenOrderAndUserException(
                        "User with ID " + request.userId() + " does not have such Order with ID " + request.orderId()));
        List<GiftCertificate> giftCertificates = giftCertificateService.findAllByIdIn(request.giftIds());
        order.getGiftCertificates().addAll(giftCertificates);
        BigDecimal sum = getSum(order.getGiftCertificates());
        order.setPrice(sum);
        OrderResponse response;
        try {
            response = orderMapper.toDto(orderRepository.saveAndFlush(order));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyHaveThisCertificateException("Your already have this GiftCertificate");
        }
        log.info("addToYourOrder {}", response);
        return response;
    }

    /**
     * Deletes one {@link Order} by its ID and {@link User} ID.
     *
     * @param userId  the ID of the User.
     * @param orderId the ID of the Order.
     * @throws NoRelationBetweenOrderAndUserException if User has no relations with Order.
     */
    @Override
    @Transactional
    public void deleteUserOrder(Long userId, Long orderId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new NoRelationBetweenOrderAndUserException(
                        "User with ID " + userId + " does not have such Order with ID " + orderId));
        orderRepository.delete(order);
        log.info("delete {}", order);
    }

    /**
     * Calculates the total sum of all gift certificates.
     *
     * @param giftCertificates the list of {@link GiftCertificate}.
     * @return the {@link BigDecimal} of total sum.
     */
    private static BigDecimal getSum(List<GiftCertificate> giftCertificates) {
        return giftCertificates.stream()
                .map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
