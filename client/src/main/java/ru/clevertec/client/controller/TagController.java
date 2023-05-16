package ru.clevertec.client.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.client.apiclient.TagApiClient;
import ru.clevertec.client.dto.TagDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagApiClient tagApiClient;

    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findTagById(@PathVariable Long id) {
        ResponseEntity<TagDto> response = ResponseEntity.ok(tagApiClient.findTagById(id));
        log.info("findById: id={}, \nresponse={}", id, response);
        return response;
    }

}
