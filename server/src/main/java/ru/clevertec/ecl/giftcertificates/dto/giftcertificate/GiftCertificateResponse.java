package ru.clevertec.ecl.giftcertificates.dto.giftcertificate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "price",
        "duration",
        "create_date",
        "last_update_date",
        "tags"
})
public record GiftCertificateResponse(Long id,
                                      String name,
                                      String description,
                                      BigDecimal price,
                                      Integer duration,

                                      @JsonProperty("create_date")
                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
                                      LocalDateTime createDate,

                                      @JsonProperty("last_update_date")
                                      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
                                      LocalDateTime lastUpdateDate,

                                      List<TagDto> tags
) {
}
