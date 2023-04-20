package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.dto.GiftCertificateDto;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchGiftCertificateException;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateMapper giftCertificateMapper = Mappers.getMapper(GiftCertificateMapper.class);

    @Override
    public List<GiftCertificateDto> findAll() {
        List<GiftCertificateDto> giftCertificates = giftCertificateDao.findAll()
                .stream()
                .sorted(Comparator.comparing(GiftCertificate::getId))
                .map(giftCertificateMapper::toDto)
                .toList();
        log.info("findAll {} GiftCertificates", giftCertificates.size());
        return giftCertificates;
    }

    @Override
    public GiftCertificateDto findById(Long id) {
        GiftCertificateDto giftCertificateDto = giftCertificateDao.findById(id)
                .map(giftCertificateMapper::toDto)
                .orElseThrow(() -> new NoSuchGiftCertificateException("GiftCertificate with ID " + id + " does not exist"));
        log.info("findById {}", giftCertificateDto);
        return giftCertificateDto;
    }

    @Override
    public GiftCertificateDto save(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateMapper.fromDto(giftCertificateDto);
        GiftCertificate saved = giftCertificateDao.save(giftCertificate);
        GiftCertificateDto savedDto = giftCertificateMapper.toDto(saved);
        log.info("save {}", savedDto);
        return savedDto;
    }

    @Override
    public GiftCertificateDto update(GiftCertificateDto giftCertificateDto) {
        GiftCertificate giftCertificate = giftCertificateMapper.fromDto(giftCertificateDto);
        GiftCertificate updated = giftCertificateDao.update(giftCertificate);
        GiftCertificateDto updatedDto = giftCertificateMapper.toDto(updated);
        log.info("update {}", updatedDto);
        return updatedDto;
    }

    @Override
    public void delete(Long id) {
        Integer delete = giftCertificateDao.delete(id);
        if (delete == 0) {
            throw new NoSuchGiftCertificateException("There is no GiftCertificate with ID " + id + " to delete");
        }
        log.info("delete #{}", id);
    }

}
