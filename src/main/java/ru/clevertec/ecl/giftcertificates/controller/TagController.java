package ru.clevertec.ecl.giftcertificates.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
import ru.clevertec.ecl.giftcertificates.swagger.TagSwagger;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController implements TagSwagger {

    private final TagService tagService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<TagDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.findById(id));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<TagDto>> findAll(@Valid TagPageRequest request) {
        return ResponseEntity.ok(tagService.findAll(request));
    }

    @Override
    @PostMapping
    public ResponseEntity<TagDto> save(@RequestBody @Valid TagDto tagDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.save(tagDto));
    }

    @Override
    @PutMapping
    public ResponseEntity<TagDto> update(@RequestBody @Valid TagDto tagDto) {
        return ResponseEntity.ok(tagService.update(tagDto));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.ok(new DeleteResponse("Tag with ID " + id + " was successfully deleted"));
    }

    @Override
    @GetMapping("/user/{userId}")
    public ResponseEntity<TagDto> findTheMostWidelyUsedWithTheHighestCost(@PathVariable Long userId) {
        return ResponseEntity.ok(tagService.findTheMostWidelyUsedWithTheHighestCost(userId));
    }

}
