package ru.clevertec.ecl.giftcertificates.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record UpdateYourOrderRequest(@Positive(message = "User ID must be greater than 0")
                                     @JsonProperty("user_id")
                                     Long userId,

                                     @Positive(message = "Order ID must be greater than 0")
                                     @JsonProperty("order_id")
                                     Long orderId,

                                     @JsonProperty("gift_ids")
                                     List<@Positive(message = "GiftCertificate ID must be greater than 0") Long> giftIds) {
}
