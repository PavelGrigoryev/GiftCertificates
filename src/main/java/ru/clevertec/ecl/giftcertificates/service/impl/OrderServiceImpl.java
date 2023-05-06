package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
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
    public OrderDto makeAnOrder(MakeAnOrderRequest request) {
        User user = userMapper.fromDto(userService.findById(request.userId()));
        List<GiftCertificate> giftCertificates = giftCertificateService.findAllByIdIn(request.giftIds());
        BigDecimal sum = giftCertificates.stream()
                .map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order order = Order.builder()
                .price(sum)
                .user(user)
                .giftCertificates(giftCertificates)
                .build();
        OrderDto dto = orderMapper.toDto(orderRepository.save(order));
        log.info("makeAnOrder {}", dto);
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> findAllByUserId(Long id, OrderPageRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.getSortBy()));
        List<OrderDto> orders = orderRepository.findAllByUserId(id, pageRequest).stream()
                .map(orderMapper::toDto)
                .toList();
        log.info("findAllByUserId {} Orders size", orders.size());
        return orders;
    }

}
