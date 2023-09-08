package ru.clevertec.ecl.giftcertificates.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.giftcertificates.dto.UserDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.UserPageRequest;
import ru.clevertec.ecl.giftcertificates.service.UserService;
import ru.clevertec.ecl.giftcertificates.controller.openapi.UserOpenApi;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserOpenApi {

    private final UserService userService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable Long id) {
        ResponseEntity<UserDto> response = ResponseEntity.ok(userService.findById(id));
        log.info("findById: id={}, \nresponse={}", id, response);
        return response;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<UserDto>> findAll(@Valid UserPageRequest request) {
        ResponseEntity<List<UserDto>> response = ResponseEntity.ok(userService.findAll(request));
        log.info("findAll: request={}, \nresponse={}", request, response);
        return response;
    }

}
