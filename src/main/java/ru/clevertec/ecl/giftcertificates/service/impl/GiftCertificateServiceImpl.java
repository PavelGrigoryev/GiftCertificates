package ru.clevertec.ecl.giftcertificates.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateRequest;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.GiftCertificateResponse;
import ru.clevertec.ecl.giftcertificates.dto.giftcertificate.PriceDurationUpdateRequest;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchGiftCertificateException;
import ru.clevertec.ecl.giftcertificates.exception.NoTagWithTheSameNameException;
import ru.clevertec.ecl.giftcertificates.mapper.GiftCertificateMapper;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.repository.GiftCertificateRepository;
import ru.clevertec.ecl.giftcertificates.service.GiftCertificateService;
import ru.clevertec.ecl.giftcertificates.service.TagService;

import java.util.List;

/**
 * The GiftCertificateServiceImpl class implements GiftCertificateService interface and provides the implementation
 * for CRUD operations on the {@link GiftCertificate} entity and also adds new functionality, such as convert to dto
 * {@link GiftCertificateResponse} from entity and from dto {@link GiftCertificateRequest} to entity. It uses a
 * {@link GiftCertificateRepository} to interact with the database and {@link GiftCertificateMapper} to map entity
 * to dto and from dto to entity. For manage transactions it uses annotation {@link Transactional}.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;
    private final GiftCertificateMapper giftCertificateMapper;
    private final EntityManager entityManager;

    /**
     * Finds one {@link GiftCertificateResponse} by ID.
     *
     * @param id the ID of the {@link GiftCertificate}.
     * @return GiftCertificateResponse with the specified ID and mapped from GiftCertificate entity.
     * @throws NoSuchGiftCertificateException if GiftCertificate is not exists by finding it by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public GiftCertificateResponse findById(Long id) {
        GiftCertificateResponse giftCertificateResponse = giftCertificateRepository.findById(id)
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
    @Transactional(readOnly = true)
    public List<GiftCertificateResponse> findAllWithTags(String tagName, String part, String sortBy, String order) {
        String sort = "date".equalsIgnoreCase(sortBy) ? "createDate" : "name";
        Sort by = sortBy != null ? Sort.by(sort) : Sort.by("id");
        by = "DESC".equalsIgnoreCase(order) ? by.descending() : by.ascending();
        List<GiftCertificate> giftCertificates = tagName != null
                ? giftCertificateRepository.findAllWithTags(tagName, part, by)
                : giftCertificateRepository.findAllWithTags(part, by);
        List<GiftCertificateResponse> giftCertificateResponses = giftCertificates.stream()
                .map(giftCertificateMapper::toResponse)
                .toList();
        log.info("findAllWithTags {} GiftCertificates size", giftCertificateResponses.size());
        return giftCertificateResponses;
    }

    /**
     * Saves one {@link GiftCertificate}.
     *
     * @param giftCertificateRequest the {@link GiftCertificateRequest} which will be mapped to GiftCertificate
     *                               and saved in database by repository.
     * @return the saved {@link GiftCertificateResponse} which was mapped from GiftCertificate entity.
     * @throws NoTagWithTheSameNameException if Tag is already exist with the same name.
     */
    @Override
    public GiftCertificateResponse save(GiftCertificateRequest giftCertificateRequest) {
        GiftCertificate giftCertificate = giftCertificateMapper.fromRequest(giftCertificateRequest);
        List<Tag> byNameIn = tagService.findByNameIn(giftCertificate.getTags()
                .stream()
                .map(Tag::getName)
                .toList());
        giftCertificate.getTags().forEach(tag -> byNameIn
                .forEach(byName -> {
                    if (tag.getName().equals(byName.getName())) {
                        tag.setId(byName.getId());
                    }
                }));
        try {
            giftCertificate = entityManager.merge(giftCertificate);
        } catch (PersistenceException | IllegalStateException e) {
            throw new NoTagWithTheSameNameException("There should be no tags with the same name!");
        }
        GiftCertificate saved = giftCertificateRepository.save(giftCertificate);
        GiftCertificateResponse savedDto = giftCertificateMapper.toResponse(saved);
        log.info("save {}", savedDto);
        return savedDto;
    }

    /**
     * Updates one {@link GiftCertificate}.
     *
     * @param request the {@link PriceDurationUpdateRequest} which will be mapped to GiftCertificate and
     *                updated in database by repository (updated only fields, that pass in PriceDurationUpdateRequest).
     * @return the updated {@link GiftCertificateResponse} which was mapped from GiftCertificate entity.
     * @throws NoSuchGiftCertificateException if GiftCertificate is not exists by finding it by ID.
     */
    @Override
    public GiftCertificateResponse update(PriceDurationUpdateRequest request) {
        GiftCertificate byId = giftCertificateRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchGiftCertificateException("GiftCertificate with ID " + request.id() + " does not exist"));
        GiftCertificate giftCertificate = createUpdatedGiftCertificate(request, byId);
        GiftCertificate updated = giftCertificateRepository.saveAndFlush(giftCertificate);
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
        GiftCertificate giftCertificate = giftCertificateRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchGiftCertificateException("There is no GiftCertificate with ID " + id + " to delete"));
        giftCertificateRepository.delete(giftCertificate);
        log.info("delete {}", giftCertificate);
    }

    /**
     * Finds All {@link GiftCertificate} by its ids.
     *
     * @param ids the List of ids.
     * @return List of GiftCertificates.
     */
    @Override
    public List<GiftCertificate> findAllByIdIn(List<Long> ids) {
        List<GiftCertificate> allByIdIn = giftCertificateRepository.findAllByIdIn(ids);
        log.info("findAllByIdIn {}", allByIdIn);
        return allByIdIn;
    }

    /**
     * Create {@link GiftCertificate} for update in database. Check price and duration fields for null, if they null
     * - sets old fields from GiftCertificate that was found by ID.
     *
     * @param request the request from {@link PriceDurationUpdateRequest}.
     * @param byId    the existing GiftCertificate that was found by ID.
     * @return the created GiftCertificate
     */
    private static GiftCertificate createUpdatedGiftCertificate(PriceDurationUpdateRequest request, GiftCertificate byId) {
        return GiftCertificate.builder()
                .id(byId.getId())
                .name(byId.getName())
                .description(byId.getDescription())
                .price(request.price() != null ? request.price() : byId.getPrice())
                .duration(request.duration() != null ? request.duration() : byId.getDuration())
                .createDate(byId.getCreateDate())
                .tags(byId.getTags())
                .build();
    }

}
