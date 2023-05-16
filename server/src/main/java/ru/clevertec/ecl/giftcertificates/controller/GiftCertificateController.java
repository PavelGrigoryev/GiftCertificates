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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.ecl.giftcertificates.dto.DeleteResponse;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.PriceDurationUpdateRequest;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;
import ru.clevertec.ecl.giftcertificates.controller.openapi.GiftCertificateOpenApi;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/gift-certificates")
public class GiftCertificateController implements GiftCertificateOpenApi {

    private final GiftCertificateService giftCertificateService;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<GiftCertificateResponse> findById(@PathVariable Long id) {
        ResponseEntity<GiftCertificateResponse> response = ResponseEntity.ok(giftCertificateService.findById(id));
        log.info("findById: id={}, \nresponse={}", id, response);
        return response;
    }

    @Override
    @GetMapping
    public ResponseEntity<List<GiftCertificateResponse>> findAllWithTags(@RequestParam(required = false) String tagName,
                                                                         @RequestParam(required = false) String part,
                                                                         @RequestParam(required = false) String sortBy,
                                                                         @RequestParam(required = false) String order) {
        ResponseEntity<List<GiftCertificateResponse>> response =
                ResponseEntity.ok(giftCertificateService.findAllWithTags(tagName, part, sortBy, order));
        log.info("findAllWithTags: tagName={}, part={}, sortBy={}, order={}, \nresponse={}",
                tagName, part, sortBy, order, response);
        return response;
    }

    @Override
    @PostMapping
    public ResponseEntity<GiftCertificateResponse> save(@RequestBody @Valid GiftCertificateRequest giftCertificateRequest) {
        ResponseEntity<GiftCertificateResponse> response =
                ResponseEntity.status(HttpStatus.CREATED).body(giftCertificateService.save(giftCertificateRequest));
        log.info("save: request={}, \nresponse={}", giftCertificateRequest, response);
        return response;
    }

    @Override
    @PutMapping
    public ResponseEntity<GiftCertificateResponse> update(@RequestBody @Valid PriceDurationUpdateRequest request) {
        ResponseEntity<GiftCertificateResponse> response = ResponseEntity.ok(giftCertificateService.update(request));
        log.info("update: request={}, \nresponse={}", request, response);
        return response;
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable Long id) {
        giftCertificateService.delete(id);
        ResponseEntity<DeleteResponse> response =
                ResponseEntity.ok(new DeleteResponse("GiftCertificate with ID " + id + " was successfully deleted"));
        log.info("delete: id={}, \nresponse={}", id, response);
        return response;
    }

}
