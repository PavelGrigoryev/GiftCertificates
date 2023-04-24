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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gift_certificates")
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

    @GetMapping("/findAllWithTags")
    public ResponseEntity<List<GiftCertificateResponse>> findAllWithTags(@RequestParam(required = false) String tagName,
                                                                         @RequestParam(required = false) String part,
                                                                         @RequestParam(required = false) String sortBy,
                                                                         @RequestParam(required = false) String order) {
        return ResponseEntity.ok(giftCertificateService.findAllWithTags(tagName, part, sortBy, order));
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
