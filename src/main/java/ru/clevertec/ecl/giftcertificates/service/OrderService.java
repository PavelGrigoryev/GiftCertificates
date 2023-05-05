package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.OrderDto;

public interface OrderService {

    OrderDto makeAnOrder(Long userId, Long giftId);

}
