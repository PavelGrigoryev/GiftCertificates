package ru.clevertec.ecl.giftcertificates.dto;

import java.math.BigDecimal;

public record PriceDurationUpdateRequest(Long id,
                                         BigDecimal price,
                                         Integer duration) {
}
