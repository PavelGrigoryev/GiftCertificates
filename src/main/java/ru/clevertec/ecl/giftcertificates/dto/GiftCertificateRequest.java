package ru.clevertec.ecl.giftcertificates.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftCertificateRequest {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer duration;

    @Builder.Default
    private List<TagDto> tags = new ArrayList<>();

}
