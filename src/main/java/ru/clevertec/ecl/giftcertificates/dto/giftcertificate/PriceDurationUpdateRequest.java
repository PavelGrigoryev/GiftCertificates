package ru.clevertec.ecl.giftcertificates.dto.giftcertificate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PriceDurationUpdateRequest(

        @Positive(message = "ID must be greater than 0")
        Long id,

        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.01", message = "Price must be greater than or equal to 0.01")
        BigDecimal price,

        @NotNull(message = "Duration cannot be null")
        @Positive(message = "Duration must be greater than 0")
        Integer duration) {
}
