package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchGiftCertificateException;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;

    @Override
    public List<GiftCertificate> findAll() {
        return giftCertificateDao.findAll()
                .stream()
                .sorted(Comparator.comparing(GiftCertificate::getId))
                .toList();
    }

    @Override
    public GiftCertificate findById(Long id) {
        return giftCertificateDao.findById(id)
                .orElseThrow(() -> new NoSuchGiftCertificateException("GiftCertificate with ID " + id + " does not exist"));
    }

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        return giftCertificateDao.save(giftCertificate);
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        return giftCertificateDao.update(giftCertificate);
    }

    @Override
    public void delete(Long id) {
        Integer delete = giftCertificateDao.delete(id);
        if (delete == 0) {
            throw new NoSuchGiftCertificateException("There is no GiftCertificate with ID " + id + " to delete");
        }
    }

}
