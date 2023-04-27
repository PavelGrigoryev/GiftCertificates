package ru.clevertec.ecl.giftcertificates.util.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.util.TestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aGiftCertificate")
@With
public class GiftCertificateTestBuilder implements TestBuilder<GiftCertificate> {

    private Long id = 1L;
    private String name = "Satisfaction";
    private String description = "Oh my!";
    private BigDecimal price = BigDecimal.TEN;
    private Integer duration = 5;
    private LocalDateTime createDate = LocalDateTime.
            of(2008, Month.APRIL, 1, 12, 30, 55, 40000);
    private LocalDateTime lastUpdateDate = LocalDateTime
            .of(2008, Month.MAY, 9, 9, 23, 14, 22500);
    private List<Tag> tags = new ArrayList<>(List.of(
            TagTestBuilder.aTag().build(),
            TagTestBuilder.aTag().withId(2L).withName("Rick").build(),
            TagTestBuilder.aTag().withId(3L).withName("Morty").build()
    ));

    @Override
    public GiftCertificate build() {
        return GiftCertificate.builder()
                .id(id)
                .name(name)
                .description(description)
                .price(price)
                .duration(duration)
                .createDate(createDate)
                .lastUpdateDate(lastUpdateDate)
                .tags(tags)
                .build();
    }

}
