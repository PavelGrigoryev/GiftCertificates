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

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    @Override
    public OrderResponse makeAnOrder(MakeAnOrderRequest request) {
        User user = userMapper.fromDto(userService.findById(request.userId()));
        List<GiftCertificate> giftCertificates = giftCertificateService.findAllByIdIn(request.giftIds());
        BigDecimal sum = getSum(giftCertificates);
        Order order = Order.builder()
                .price(sum)
                .user(user)
                .giftCertificates(giftCertificates)
                .build();
        OrderResponse response = orderMapper.toDto(orderRepository.save(order));
        log.info("makeAnOrder {}", response);
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllByUserId(Long id, OrderPageRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.getSortBy()));
        List<OrderResponse> orders = orderRepository.findAllByUserId(id, pageRequest).stream()
                .map(orderMapper::toDto)
                .toList();
        log.info("findAllByUserId {} Orders size", orders.size());
        return orders;
    }

    @Override
    public OrderResponse addToYourOrder(UpdateYourOrderRequest request) {
        Order order = orderRepository.findOrderByIdAndUserId(request.orderId(), request.userId())
                .orElseThrow(() -> new NoRelationBetweenOrderAndUserException(
                        "User with ID " + request.userId() + " does not have such Order with ID " + request.orderId()));
        List<GiftCertificate> giftCertificates = giftCertificateService.findAllByIdIn(request.giftIds());
        order.getGiftCertificates().addAll(giftCertificates);
        BigDecimal sum = getSum(order.getGiftCertificates());
        order.setPrice(sum);
        OrderResponse response;
        try {
            response = orderMapper.toDto(orderRepository.save(order));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyHaveThisCertificateException("Your already have this GiftCertificate");
        }
        log.info("addToYourOrder {}", response);
        return response;
    }

    @Override
    public void delete(Long userId, Long orderId) {
        Order order = orderRepository.findOrderByIdAndUserId(orderId, userId)
                .orElseThrow(() -> new NoRelationBetweenOrderAndUserException(
                        "User with ID " + userId + " does not have such Order with ID " + orderId));
        orderRepository.delete(order);
        log.info("delete {}", order);
    }

    private static BigDecimal getSum(List<GiftCertificate> giftCertificates) {
        return giftCertificates.stream()
                .map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
