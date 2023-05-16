package ru.clevertec.ecl.giftcertificates.util.impl;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Order;
import ru.clevertec.ecl.giftcertificates.util.TestBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aOrder")
@With
public class OrderTestBuilder implements TestBuilder<Order> {

    private Long id = 1L;
    private BigDecimal price = BigDecimal.TEN;
    private LocalDateTime purchaseTime = LocalDateTime.
            of(2020, Month.APRIL, 25, 12, 30, 55);
    private LocalDateTime lastAdditionTime = LocalDateTime.
            of(2021, Month.JULY, 14, 15, 12, 47);
    private List<GiftCertificate> giftCertificates = new ArrayList<>(List.of(
            GiftCertificateTestBuilder.aGiftCertificate().withTags(
                    List.of(TagTestBuilder.aTag().build())
            ).build(),
            GiftCertificateTestBuilder.aGiftCertificate().withId(2L).withName("Jingle").build()
    ));

    @Override
    public Order build() {
        return Order.builder()
                .id(id)
                .price(price)
                .purchaseTime(purchaseTime)
                .lastAdditionTime(lastAdditionTime)
                .giftCertificates(giftCertificates)
                .build();
    }

}
