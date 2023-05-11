package ru.clevertec.ecl.giftcertificates.dto.giftcertificate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PriceDurationUpdateRequest(@Positive(message = "ID must be greater than 0")
                                         Long id,

                                         @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
                                         BigDecimal price,

                                         @Positive(message = "Duration must be greater than 0")
                                         Integer duration) {
}
