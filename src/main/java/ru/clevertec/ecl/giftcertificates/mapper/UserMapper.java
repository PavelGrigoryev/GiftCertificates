package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.model.User;

@Mapper(componentModel = "spring",
        uses = {OrderMapper.class, GiftCertificateMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDto toDto(User user);

    User fromDto(UserDto userDto);

}
