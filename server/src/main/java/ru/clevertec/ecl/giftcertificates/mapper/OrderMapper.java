package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.giftcertificates.dto.order.OrderResponse;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Order;
import ru.clevertec.ecl.giftcertificates.model.User;

import java.math.BigDecimal;
import java.util.List;

@Mapper(uses = GiftCertificateMapper.class)
public interface OrderMapper {

    OrderResponse toDto(Order order);

    default Order createOrder(User user, List<GiftCertificate> giftCertificates, BigDecimal sum) {
        return Order.builder()
                .price(sum)
                .user(user)
                .giftCertificates(giftCertificates)
                .build();
    }

}
