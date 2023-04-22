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
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * The GiftCertificateServiceImpl class implements GiftCertificateService interface and provides the implementation
 * for CRUD operations on the {@link GiftCertificate} entity and also adds new functionality, such as convert to dto
 * {@link GiftCertificateResponse} from entity and from dto {@link GiftCertificateRequest} to entity. It uses a
 * {@link GiftCertificateDao} to interact with the database and {@link GiftCertificateMapper} to map entity to dto and
 * from dto to entity.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final GiftCertificateMapper giftCertificateMapper = Mappers.getMapper(GiftCertificateMapper.class);

    /**
     * Finds all {@link GiftCertificateResponse}.
     *
     * @return a sorted by id and mapped from entity to dto list of all GiftCertificateResponse.
     */
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

    /**
     * Finds one {@link GiftCertificateResponse} by ID.
     *
     * @param id the ID of the {@link GiftCertificate}.
     * @return GiftCertificateResponse with the specified ID and mapped from GiftCertificate entity.
     * @throws NoSuchGiftCertificateException if GiftCertificate is not exists by finding it by ID.
     */
    @Override
    public GiftCertificateResponse findById(Long id) {
        GiftCertificateResponse giftCertificateResponse = giftCertificateDao.findById(id)
                .map(giftCertificateMapper::toResponse)
                .orElseThrow(() -> new NoSuchGiftCertificateException("GiftCertificate with ID " + id + " does not exist"));
        log.info("findById {}", giftCertificateResponse);
        return giftCertificateResponse;
    }

    /**
     * Finds all {@link GiftCertificateResponse} by the specified parameters (All params are optional and can be used
     * in conjunction).
     *
     * @param tagName the name of {@link Tag}.
     * @param part    the part of name or description of {@link GiftCertificate}.
     * @param sortBy  the sort by date or by name of GiftCertificate.
     * @param order   the order for sorting ascending(ASC) or descending(DESC).
     * @return a filtered and sorted by one or many parameters list of GiftCertificateResponse.
     */
    @Override
    public List<GiftCertificateResponse> findAllWithTags(String tagName, String part, String sortBy, String order) {
        List<GiftCertificateResponse> giftCertificates = giftCertificateDao.findAllWithTags(tagName, part, sortBy, order)
                .stream()
                .map(giftCertificateMapper::toResponse)
                .toList();
        log.info("findAllWithTags {} GiftCertificates size", giftCertificates.size());
        return giftCertificates;
    }

    /**
     * Saves one {@link GiftCertificate}.
     *
     * @param giftCertificateRequest the {@link GiftCertificateRequest} which will be mapped to GiftCertificate
     *                               and saved in database by dao.
     * @return the saved {@link GiftCertificateResponse} which was mapped from GiftCertificate entity.
     */
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

    /**
     * Updates one {@link GiftCertificate}.
     *
     * @param giftCertificateRequest the {@link GiftCertificateRequest} which will be mapped to GiftCertificate and
     *                               updated in database by dao (updated only fields, that pass in GiftCertificateRequest).
     * @return the updated {@link GiftCertificateResponse} which was mapped from GiftCertificate entity.
     * @throws NoSuchGiftCertificateException if GiftCertificate is not exists by finding it by ID.
     */
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

    /**
     * Deletes one {@link GiftCertificate} by ID.
     *
     * @param id the ID of the GiftCertificate.
     * @throws NoSuchGiftCertificateException if GiftCertificate is not exists by finding it by ID.
     */
    @Override
    public void delete(Long id) {
        GiftCertificate giftCertificate = giftCertificateDao.delete(id)
                .orElseThrow(() ->
                        new NoSuchGiftCertificateException("There is no GiftCertificate with ID " + id + " to delete"));
        log.info("delete {}", giftCertificate);
    }

    /**
     * Checks all fields of {@link GiftCertificate} for null, if they - null sets old fields from GiftCertificate that
     * was found by ID.
     *
     * @param giftCertificate the GiftCertificate from {@link GiftCertificateRequest}.
     * @param byId            the existing GiftCertificate that was found by ID.
     */
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
