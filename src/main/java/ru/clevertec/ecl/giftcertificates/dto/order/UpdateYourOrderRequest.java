package ru.clevertec.ecl.giftcertificates.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UpdateYourOrderRequest(@JsonProperty("user_id")
                                     Long userId,

                                     @JsonProperty("order_id")
                                     Long orderId,

                                     @JsonProperty("gift_ids")
                                     List<Long> giftIds) {
}
