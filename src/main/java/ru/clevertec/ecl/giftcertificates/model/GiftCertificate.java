package ru.clevertec.ecl.giftcertificates.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificate {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;
    @JsonProperty("create_date")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime createDate;
    @JsonProperty("last_update_date")
    @JsonFormat
            (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss:SSS")
    private LocalDateTime lastUpdateDate;

}
