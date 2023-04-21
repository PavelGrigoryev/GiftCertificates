package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateResponse;

import java.util.List;

public interface GiftCertificateService {

    List<GiftCertificateResponse> findAll();

    GiftCertificateResponse findById(Long id);

    List<GiftCertificateResponse> findAllByTagName(String tagName);

    List<GiftCertificateResponse> findAllByPartOfNameOrDescription(String part);

    List<GiftCertificateResponse> findAllSortedByCreateDateAndName(String asc);

    GiftCertificateResponse save(GiftCertificateRequest giftCertificateRequest);

    GiftCertificateResponse update(GiftCertificateRequest giftCertificateRequest);

    void delete(Long id);

}
