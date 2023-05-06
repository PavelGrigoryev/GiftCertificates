package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.PriceDurationUpdateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;

import java.util.List;

public interface GiftCertificateService {

    GiftCertificateResponse findById(Long id);

    List<GiftCertificateResponse> findAllWithTags(String tagName, String part, String sortBy, String order);

    GiftCertificateResponse save(GiftCertificateRequest giftCertificateRequest);

    GiftCertificateResponse update(PriceDurationUpdateRequest request);

    void delete(Long id);

}
