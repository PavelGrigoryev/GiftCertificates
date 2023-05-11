package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.PriceDurationUpdateRequest;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

@Mapper(uses = TagMapper.class)
public interface GiftCertificateMapper {

    GiftCertificateResponse toResponse(GiftCertificate giftCertificate);

    GiftCertificate fromResponse(GiftCertificateResponse giftCertificateResponse);

    GiftCertificate fromRequest(GiftCertificateRequest giftCertificateRequest);

    GiftCertificateRequest toRequest(GiftCertificate giftCertificate);

    /**
     * Create {@link GiftCertificate} for update in database. Check price and duration fields for null, if they null
     * - sets old fields from GiftCertificate that was found by ID.
     *
     * @param request the request from {@link PriceDurationUpdateRequest}.
     * @param byId    the existing GiftCertificate that was found by ID.
     * @return the created GiftCertificate
     */
    default GiftCertificate createUpdatedGiftCertificate(PriceDurationUpdateRequest request, GiftCertificate byId) {
        return GiftCertificate.builder()
                .id(byId.getId())
                .name(byId.getName())
                .description(byId.getDescription())
                .price(request.price() != null ? request.price() : byId.getPrice())
                .duration(request.duration() != null ? request.duration() : byId.getDuration())
                .createDate(byId.getCreateDate())
                .tags(byId.getTags())
                .build();
    }

}
