package ru.clevertec.ecl.giftcertificates.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "price",
        "purchase_time",
        "gift_certificate"
})
public record OrderResponse(Long id,
                            BigDecimal price,

                            @JsonProperty("purchase_time")
                            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
                            LocalDateTime purchaseTime,

                            @JsonProperty("gift_certificates")
                            List<GiftCertificateResponse> giftCertificates) {
}
