package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dto.OrderDto;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapper;
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
    private final GiftCertificateMapper giftCertificateMapper;

    @Override
    public OrderDto makeAnOrder(Long userId, Long giftId) {
        User user = userMapper.fromDto(userService.findById(userId));
        GiftCertificate giftCertificate = giftCertificateMapper.fromResponse(giftCertificateService.findById(giftId));
        Order order = Order.builder()
                .price(BigDecimal.TEN)
                .user(user)
                .giftCertificate(giftCertificate)
                .build();
        OrderDto dto = orderMapper.toDto(orderRepository.save(order));
        log.info("makeAnOrder {}", dto);
        return dto;
    }

}
