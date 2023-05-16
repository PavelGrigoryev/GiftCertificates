package ru.clevertec.ecl.giftcertificates.model.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

import java.time.LocalDateTime;

public class GiftCertificateListener {

    @PrePersist
    public void prePersist(GiftCertificate giftCertificate) {
        giftCertificate.setOperation("INSERT");
        LocalDateTime now = LocalDateTime.now();
        giftCertificate.setCreateDate(now);
        giftCertificate.setLastUpdateDate(now);
    }

    @PreUpdate
    public void preUpdate(GiftCertificate giftCertificate) {
        giftCertificate.setOperation("UPDATE");
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
    }

}
