package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchTagException;
import ru.clevertec.ecl.giftcertificates.exception.NoTagWithTheSameNameException;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapper;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.repository.TagRepository;
import ru.clevertec.ecl.giftcertificates.service.TagService;

import java.util.Comparator;
import java.util.List;

/**
 * The TagServiceImpl class implements TagService interface and provides the implementation for CRUD operations on the
 * {@link Tag} entity and also adds new functionality, such as convert to dto {@link TagDto} from entity and from
 * dto to entity. It uses a {@link TagRepository} to interact with the database and {@link TagMapper} to map entity
 * to dto and from dto to entity. For manage transactions it uses annotation {@link Transactional}.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    /**
     * Finds one {@link TagDto} by ID.
     *
     * @param id the ID of the {@link Tag}.
     * @return TagDto with the specified ID and mapped from Tag entity.
     * @throws NoSuchTagException if Tag is not exists by finding it by ID.
     */
    @Override
    @Transactional(readOnly = true)
    public TagDto findById(Long id) {
        TagDto tagDto = tagRepository.findById(id)
                .map(tagMapper::toDto)
                .orElseThrow(() -> new NoSuchTagException("Tag with ID " + id + " does not exist"));
        log.info("findById {}", tagDto);
        return tagDto;
    }

    /**
     * Finds all {@link TagDto}.
     *
     * @return a sorted by id and mapped from entity to dto list of all TagDto.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TagDto> findAll() {
        List<TagDto> tags = tagRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Tag::getId))
                .map(tagMapper::toDto)
                .toList();
        log.info("findAll {} Tags size", tags.size());
        return tags;
    }

    /**
     * Saves one {@link Tag}.
     *
     * @param tagDto the {@link TagDto} which will be mapped to Tag and saved in database by repository.
     * @return the saved TagDto which was mapped from Tag entity.
     * @throws NoTagWithTheSameNameException if Tag is already exist with the same name.
     */
    @Override
    public TagDto save(TagDto tagDto) {
        Tag tag = tagMapper.fromDto(tagDto);
        Tag saved;
        try {
            saved = tagRepository.save(tag);
        } catch (DataIntegrityViolationException e) {
            throw new NoTagWithTheSameNameException("Tag name " + tag.getName() + " is already exist! It must be unique!");
        }
        TagDto savedDto = tagMapper.toDto(saved);
        log.info("save {}", savedDto);
        return savedDto;
    }

    /**
     * Updates one {@link Tag}.
     *
     * @param tagDto the {@link TagDto} which will be mapped to Tag and updated in database by repository.
     * @return the updated TagDto which was mapped from Tag entity, if name is same - returns same TagDto without update.
     * @throws NoSuchTagException if Tag is not exists by finding it by ID.
     * @throws NoTagWithTheSameNameException if Tag is already exist with the same name.
     */
    @Override
    public TagDto update(TagDto tagDto) {
        Tag tag = tagMapper.fromDto(tagDto);
        Tag byId = tagRepository.findById(tag.getId())
                .orElseThrow(() -> new NoSuchTagException("Tag with ID " + tag.getId() + " does not exist"));
        if (tag.getName().equals(byId.getName())) {
            log.info("no update {}", tagDto);
            return tagDto;
        }
        Tag updated;
        try {
            updated = tagRepository.saveAndFlush(tag);
        } catch (DataIntegrityViolationException e) {
            throw new NoTagWithTheSameNameException("Tag name " + tag.getName() + " is already exist! It must be unique!");
        }
        TagDto updatedDto = tagMapper.toDto(updated);
        log.info("update {}", updatedDto);
        return updatedDto;
    }

    /**
     * Deletes one {@link Tag} by ID.
     *
     * @param id the ID of the Tag.
     * @throws NoSuchTagException if Tag is not exists by finding it by ID.
     */
    @Override
    public void delete(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new NoSuchTagException("There is no Tag with ID " + id + " to delete"));
        tagRepository.deleteRelation(id);
        tagRepository.deleteById(id);
        log.info("delete {}", tag);
    }

}
