package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateDto;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

@Mapper
public interface GiftCertificateMapper {

    GiftCertificateDto toDto(GiftCertificate giftCertificate);

    GiftCertificate fromDto(GiftCertificateDto giftCertificateDto);

}
