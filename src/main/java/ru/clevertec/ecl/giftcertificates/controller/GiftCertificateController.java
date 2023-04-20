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
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/giftCertificates")
public class GiftCertificateController {

    private final GiftCertificateService giftCertificateService;

    @GetMapping
    public ResponseEntity<List<GiftCertificate>> findAll() {
        return ResponseEntity.ok(giftCertificateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificate> findById(@PathVariable Long id) {
        return ResponseEntity.ok(giftCertificateService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GiftCertificate> save(@RequestBody GiftCertificate giftCertificate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(giftCertificateService.save(giftCertificate));
    }

    @PutMapping
    public ResponseEntity<GiftCertificate> update(@RequestBody GiftCertificate giftCertificate) {
        return ResponseEntity.ok(giftCertificateService.update(giftCertificate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        giftCertificateService.delete(id);
        return ResponseEntity.ok("GiftCertificate with ID " + id + " was successfully deleted");
    }

}
