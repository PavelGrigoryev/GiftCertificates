package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findById(Long id);

    List<UserDto> findAll();

}
