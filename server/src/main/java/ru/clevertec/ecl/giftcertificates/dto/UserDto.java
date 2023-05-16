package ru.clevertec.ecl.giftcertificates.dto;

import ru.clevertec.ecl.giftcertificates.dto.order.OrderResponse;

import java.util.List;

public record UserDto(Long id,
                      String username,
                      List<OrderResponse> orders) {
}
