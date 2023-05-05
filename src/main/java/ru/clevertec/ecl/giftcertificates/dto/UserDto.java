package ru.clevertec.ecl.giftcertificates.dto;

import java.util.List;

public record UserDto(Long id,
                      String username,
                      List<OrderDto> orders) {
}
