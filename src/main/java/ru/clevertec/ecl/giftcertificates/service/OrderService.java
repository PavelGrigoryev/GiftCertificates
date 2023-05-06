package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto makeAnOrder(MakeAnOrderRequest request);

    List<OrderDto> findAllByUserId(Long id, OrderPageRequest request);

}
