package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

@Mapper
public interface GiftCertificateMapper {

    GiftCertificateResponse toResponse(GiftCertificate giftCertificate);

    @Mapping(target = "createDate", ignore = true)
    @Mapping(target = "lastUpdateDate", ignore = true)
    GiftCertificate fromRequest(GiftCertificateRequest giftCertificateRequest);

    GiftCertificateRequest toRequest(GiftCertificate giftCertificate);

}
