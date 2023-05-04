package ru.clevertec.ecl.giftcertificates.dto;

import java.math.BigDecimal;
import java.util.List;

public record GiftCertificateRequest(String name,
                                     String description,
                                     BigDecimal price,
                                     Integer duration,
                                     List<TagDto> tags) {
}
