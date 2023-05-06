package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

@Mapper(componentModel = "spring", uses = TagMapper.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GiftCertificateMapper {

    GiftCertificateResponse toResponse(GiftCertificate giftCertificate);

    GiftCertificate fromResponse(GiftCertificateResponse giftCertificateResponse);

    GiftCertificate fromRequest(GiftCertificateRequest giftCertificateRequest);

    GiftCertificateRequest toRequest(GiftCertificate giftCertificate);

}
