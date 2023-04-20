package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificateDto> findAll();

    GiftCertificateDto findById(Long id);

    GiftCertificateDto save(GiftCertificateDto giftCertificateDto);

    GiftCertificateDto update(GiftCertificateDto giftCertificateDto);

    void delete(Long id);

}
