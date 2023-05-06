package ru.clevertec.ecl.giftcertificates.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MakeAnOrderRequest(@JsonProperty("user_id")
                                 Long userId,

                                 @JsonProperty("gift_id")
                                 Long giftId) {
}
