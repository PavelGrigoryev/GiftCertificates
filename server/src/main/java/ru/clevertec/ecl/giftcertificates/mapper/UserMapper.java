package ru.clevertec.ecl.giftcertificates.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.model.User;

@Mapper(uses = {OrderMapper.class, GiftCertificateMapper.class})
public interface UserMapper {

    UserDto toDto(User user);

    User fromDto(UserDto userDto);

}
