package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.UserPageRequest;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchUserException;
import ru.clevertec.ecl.giftcertificates.mapper.UserMapper;
import ru.clevertec.ecl.giftcertificates.repository.UserRepository;
import ru.clevertec.ecl.giftcertificates.service.UserService;

import java.util.List;

/**
 * The UserServiceImpl class implements UserService interface and provides the implementation for CRUD operations on
 * the {@link ru.clevertec.ecl.giftcertificates.model.User} entity and also adds new functionality, such as convert to dto
 * {@link UserDto} from entity and sort it by pageable with {@link PageRequest}. It uses a {@link UserRepository} to
 * interact with the database and {@link UserMapper} to map entity to dto and from dto to entity.
 * For manage transactions it uses annotation {@link Transactional}.
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Finds one {@link UserDto} by ID.
     *
     * @param id the ID of the {@link ru.clevertec.ecl.giftcertificates.model.User}.
     * @return UserDto with the specified ID and mapped from User entity.
     * @throws NoSuchUserException if User is not exists by finding it by ID.
     */
    @Override
    public UserDto findById(Long id) {
        UserDto userDto = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new NoSuchUserException("User with ID " + id + " does not exist"));
        log.info("findById {}", userDto);
        return userDto;
    }

    /**
     * Finds all {@link UserDto} with pagination.
     *
     * @param request the {@link UserPageRequest}. Users will be sorted by its parameters and divided into pages.
     * @return a sorted by pageable and mapped from entity to dto list of all UserDto.
     */
    @Override
    public List<UserDto> findAll(UserPageRequest request) {
        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), Sort.by(request.getSortBy()));
        List<UserDto> users = userRepository.findAll(pageRequest).stream()
                .map(userMapper::toDto)
                .toList();
        log.info("findAll {} User size", users.size());
        return users;
    }

}
