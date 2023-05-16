package ru.clevertec.ecl.giftcertificates.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.giftcertificates.dto.DeleteResponse;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.dto.pagination.TagPageRequest;
import ru.clevertec.ecl.giftcertificates.service.TagService;
import ru.clevertec.ecl.giftcertificates.controller.openapi.TagOpenApi;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController implements TagOpenApi {

    private final TagService tagService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(@PathVariable Long id) {
        ResponseEntity<TagDto> response = ResponseEntity.ok(tagService.findById(id));
        log.info("findById: id={}, \nresponse={}", id, response);
        return response;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TagDto>> findAll(@Valid TagPageRequest request) {
        ResponseEntity<List<TagDto>> response = ResponseEntity.ok(tagService.findAll(request));
        log.info("findAll: request={}, \nresponse={}", request, response);
        return response;
    }

    @Override
    @PostMapping
    public ResponseEntity<TagDto> save(@RequestBody @Valid TagDto tagDto) {
        ResponseEntity<TagDto> response = ResponseEntity.status(HttpStatus.CREATED).body(tagService.save(tagDto));
        log.info("save: request={}, \nresponse={}", tagDto, response);
        return response;
    }

    @Override
    @PutMapping
    public ResponseEntity<TagDto> update(@RequestBody @Valid TagDto tagDto) {
        ResponseEntity<TagDto> response = ResponseEntity.ok(tagService.update(tagDto));
        log.info("update: request={}, \nresponse={}", tagDto, response);
        return response;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        tagService.delete(id);
        ResponseEntity<DeleteResponse> response =
                ResponseEntity.ok(new DeleteResponse("Tag with ID " + id + " was successfully deleted"));
        log.info("delete: id={}, \nresponse={}", id, response);
        return response;
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<TagDto> findTheMostWidelyUsedWithTheHighestCost(@PathVariable Long userId) {
        ResponseEntity<TagDto> response =
                ResponseEntity.ok(tagService.findTheMostWidelyUsedWithTheHighestCost(userId));
        log.info("findTheMostWidelyUsedWithTheHighestCost: id={}, \nresponse={}", userId, response);
        return response;
    }

}
