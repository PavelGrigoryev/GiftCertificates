package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchUserException;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapper;
import ru.clevertec.ecl.giftcertificates.model.User;
import ru.clevertec.ecl.giftcertificates.repository.UserRepository;
import ru.clevertec.ecl.giftcertificates.service.UserService;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto findById(Long id) {
        UserDto userDto = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NoSuchUserException("User with ID " + id + " does not exist"));
        log.info("findById {}", userDto);
        return userDto;
    }

    @Override
    public List<UserDto> findAll() {
        List<UserDto> users = userRepository.findAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .map(userMapper::toDto)
                .toList();
        log.info("findAll {} User size", users.size());
        return users;
    }

}