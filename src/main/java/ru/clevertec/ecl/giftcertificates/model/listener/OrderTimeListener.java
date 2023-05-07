package ru.clevertec.ecl.giftcertificates.model.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.clevertec.ecl.giftcertificates.model.Order;

import java.time.LocalDateTime;

public class OrderTimeListener {

    @PrePersist
    public void prePersist(Order order) {
        LocalDateTime now = LocalDateTime.now();
        order.setPurchaseTime(now);
        order.setLastAdditionTime(now);
    }

    @PreUpdate
    public void preUpdate(Order order) {
        order.setLastAdditionTime(LocalDateTime.now());
    }

}
