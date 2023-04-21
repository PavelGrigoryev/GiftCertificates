package ru.clevertec.ecl.giftcertificates.dao;

import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateDao {

    List<GiftCertificate> findAll();

    Optional<GiftCertificate> findById(Long id);

    List<GiftCertificate> findByTagName(String tagName);

    GiftCertificate save(GiftCertificate giftCertificate);

    GiftCertificate update(GiftCertificate giftCertificate);

    Optional<GiftCertificate> delete(Long id);

}
