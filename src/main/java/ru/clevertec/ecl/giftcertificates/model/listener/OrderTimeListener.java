package ru.clevertec.ecl.giftcertificates.model.listener;

import jakarta.persistence.PrePersist;
import ru.clevertec.ecl.giftcertificates.model.Order;

import java.time.LocalDateTime;

public class OrderTimeListener {

    @PrePersist
    public void prePersist(Order order) {
        order.setPurchaseTime(LocalDateTime.now());
    }

}
