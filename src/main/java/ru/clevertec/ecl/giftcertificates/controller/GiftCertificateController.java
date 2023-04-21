package ru.clevertec.ecl.giftcertificates.controller;

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
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/giftCertificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @GetMapping
    public ResponseEntity<List<GiftCertificateResponse>> findAll() {
        return ResponseEntity.ok(giftCertificateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(giftCertificateService.findById(id));
    }

    @GetMapping("/findAllByTagName/{tagName}")
    public ResponseEntity<List<GiftCertificateResponse>> findAllByTagName(@PathVariable String tagName) {
        return ResponseEntity.ok(giftCertificateService.findAllByTagName(tagName));
    }

    @GetMapping("/findAllByPartOfNameOrDescription/{part}")
    public ResponseEntity<List<GiftCertificateResponse>> findAllByPartOfNameOrDescription(@PathVariable String part) {
        return ResponseEntity.ok(giftCertificateService.findAllByPartOfNameOrDescription(part));
    }

    @GetMapping("/findAllSortedByCreateDateAndName/{asc}")
    public ResponseEntity<List<GiftCertificateResponse>> findAllSortedByCreateDateAndName(@PathVariable String asc) {
        return ResponseEntity.ok(giftCertificateService.findAllSortedByCreateDateAndName(asc));
    }

    @PostMapping
    public ResponseEntity<GiftCertificateResponse> save(@RequestBody GiftCertificateRequest giftCertificateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(giftCertificateService.save(giftCertificateRequest));
    }

    @PutMapping
    public ResponseEntity<GiftCertificateResponse> update(@RequestBody GiftCertificateRequest giftCertificateRequest) {
        return ResponseEntity.ok(giftCertificateService.update(giftCertificateRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.ok("GiftCertificate with ID " + id + " was successfully deleted");
    }

}
