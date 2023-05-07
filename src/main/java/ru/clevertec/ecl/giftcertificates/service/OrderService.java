package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.order.OrderResponse;
import ru.clevertec.ecl.giftcertificates.dto.order.UpdateYourOrderRequest;
import ru.clevertec.ecl.giftcertificates.dto.pagination.OrderPageRequest;
import ru.clevertec.ecl.giftcertificates.dto.order.MakeAnOrderRequest;

import java.util.List;

public interface OrderService {

    OrderResponse makeAnOrder(MakeAnOrderRequest request);

    List<OrderResponse> findAllByUserId(Long id, OrderPageRequest request);

    OrderResponse addToYourOrder(UpdateYourOrderRequest request);

    void delete(Long userId, Long orderId);

}
