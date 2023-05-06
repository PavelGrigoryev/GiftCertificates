package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.PaginationRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderDto;

import java.util.List;

public interface OrderService {

    OrderDto makeAnOrder(MakeAnOrderRequest request);

    List<OrderDto> findAllByUserId(Long id, PaginationRequest request);

}
