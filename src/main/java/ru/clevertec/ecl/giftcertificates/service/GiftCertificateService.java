package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificate> findAll();

    GiftCertificate findById(Long id);

    GiftCertificate save(GiftCertificate giftCertificate);

    GiftCertificate update(GiftCertificate giftCertificate);

    void delete(Long id);

}
