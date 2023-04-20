package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.model.Tag;

@Mapper
public interface TagMapper {

    TagDto toDto(Tag tag);

    Tag fromDto(TagDto tagDto);

}
