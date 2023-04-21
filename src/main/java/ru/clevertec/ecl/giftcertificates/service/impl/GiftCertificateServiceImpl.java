package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchGiftCertificateException;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateMapper giftCertificateMapper = Mappers.getMapper(GiftCertificateMapper.class);

    @Override
    public List<GiftCertificateResponse> findAll() {
        List<GiftCertificateResponse> giftCertificates = giftCertificateDao.findAll()
                .stream()
                .sorted(Comparator.comparing(GiftCertificate::getId))
                .map(giftCertificateMapper::toResponse)
                .toList();
        log.info("findAll {} GiftCertificates size", giftCertificates.size());
        return giftCertificates;
    }

    @Override
    public GiftCertificateResponse findById(Long id) {
        GiftCertificateResponse giftCertificateResponse = giftCertificateDao.findById(id)
                .map(giftCertificateMapper::toResponse)
                .orElseThrow(() -> new NoSuchGiftCertificateException("GiftCertificate with ID " + id + " does not exist"));
        log.info("findById {}", giftCertificateResponse);
        return giftCertificateResponse;
    }

    @Override
    public List<GiftCertificateResponse> findAllByTagName(String tagName) {
        List<GiftCertificateResponse> giftCertificates = giftCertificateDao.findAllByTagName(tagName)
                .stream()
                .map(giftCertificateMapper::toResponse)
                .toList();
        log.info("findAllByTagName {} GiftCertificates size", giftCertificates.size());
        return giftCertificates;
    }

    @Override
    public List<GiftCertificateResponse> findAllByPartOfNameOrDescription(String part) {
        List<GiftCertificateResponse> giftCertificates = giftCertificateDao.findAllByPartOfNameOrDescription(part)
                .stream()
                .map(giftCertificateMapper::toResponse)
                .toList();
        log.info("findAllByPartOfNameOrDescription {} GiftCertificates size", giftCertificates.size());
        return giftCertificates;
    }

    @Override
    public List<GiftCertificateResponse> findAllSortedByCreateDateAndName(String asc) {
        List<GiftCertificateResponse> giftCertificates = giftCertificateDao.findAllSortedByCreateDateAndName(asc)
                .stream()
                .map(giftCertificateMapper::toResponse)
                .toList();
        log.info("findAllSortedByCreateDateAndName {} GiftCertificates size", giftCertificates.size());
        return giftCertificates;
    }

    @Override
    public GiftCertificateResponse save(GiftCertificateRequest giftCertificateRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.fromRequest(giftCertificateRequest);
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        GiftCertificate saved = giftCertificateDao.save(giftCertificate);
        GiftCertificateResponse savedDto = giftCertificateMapper.toResponse(saved);
        log.info("save {}", savedDto);
        return savedDto;
    }

    @Override
    public GiftCertificateResponse update(GiftCertificateRequest giftCertificateRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.fromRequest(giftCertificateRequest);
        GiftCertificate byId = giftCertificateDao.findById(giftCertificate.getId())
                .orElseThrow(() -> new NoSuchGiftCertificateException("GiftCertificate with ID "
                                                                      + giftCertificate.getId() + " does not exist"));
        giftCertificate.setCreateDate(byId.getCreateDate());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        checkFieldsForNull(giftCertificate, byId);
        GiftCertificate updated = giftCertificateDao.update(giftCertificate);
        GiftCertificateResponse updatedDto = giftCertificateMapper.toResponse(updated);
        log.info("update {}", updatedDto);
        return updatedDto;
    }

    @Override
    public void delete(Long id) {
        GiftCertificate giftCertificate = giftCertificateDao.delete(id)
                .orElseThrow(() ->
                        new NoSuchGiftCertificateException("There is no GiftCertificate with ID " + id + " to delete"));
        log.info("delete {}", giftCertificate);
    }

    private static void checkFieldsForNull(GiftCertificate giftCertificate, GiftCertificate byId) {
        giftCertificate.setName(giftCertificate.getName() != null ? giftCertificate.getName() : byId.getName());
        giftCertificate.setDescription(giftCertificate.getDescription() != null
                ? giftCertificate.getDescription() : byId.getDescription());
        giftCertificate.setPrice(giftCertificate.getPrice() != null ? giftCertificate.getPrice() : byId.getPrice());
        giftCertificate.setDuration(giftCertificate.getDuration() != null
                ? giftCertificate.getDuration() : byId.getDuration());
        giftCertificate.setTags(!giftCertificate.getTags().isEmpty() ? giftCertificate.getTags() : byId.getTags());
    }

}
