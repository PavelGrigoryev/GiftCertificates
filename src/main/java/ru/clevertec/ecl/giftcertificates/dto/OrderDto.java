package ru.clevertec.ecl.giftcertificates.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonPropertyOrder({
        "id",
        "price",
        "purchase_time"
})
public record OrderDto(Long id,
                       BigDecimal price,

                       @JsonProperty("purchase_time")
                       @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
                       LocalDateTime purchaseTime) {
}
