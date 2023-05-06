package ru.clevertec.ecl.giftcertificates.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record MakeAnOrderRequest(@JsonProperty("user_id")
                                 Long userId,

                                 @JsonProperty("gift_ids")
                                 List<Long> giftIds) {
}
