package ru.clevertec.ecl.giftcertificates.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import ru.clevertec.ecl.giftcertificates.dao.TagDao;
import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.exception.NoSuchTagException;
import ru.clevertec.ecl.giftcertificates.mapper.TagMapper;
import ru.clevertec.ecl.giftcertificates.model.Tag;
import ru.clevertec.ecl.giftcertificates.service.TagService;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

    @Override
    public List<TagDto> findAll() {
        List<TagDto> tags = tagDao.findAll()
                .stream()
                .sorted(Comparator.comparing(Tag::getId))
                .map(tagMapper::toDto)
                .toList();
        log.info("findAll {} Tags size", tags.size());
        return tags;
    }

    @Override
    public TagDto findById(Long id) {
        TagDto tagDto = tagDao.findById(id)
                .map(tagMapper::toDto)
                .orElseThrow(() -> new NoSuchTagException("Tag with ID " + id + " does not exist"));
        log.info("findById {}", tagDto);
        return tagDto;
    }

    @Override
    public TagDto save(TagDto tagDto) {
        Tag tag = tagMapper.fromDto(tagDto);
        Tag saved = tagDao.save(tag);
        TagDto savedDto = tagMapper.toDto(saved);
        log.info("save {}", savedDto);
        return savedDto;
    }

    @Override
    public TagDto update(TagDto tagDto) {
        Tag tag = tagMapper.fromDto(tagDto);
        Tag byId = tagDao.findById(tag.getId()).orElseThrow(() -> new NoSuchTagException("Tag with ID "
                                                                              + tag.getId() + " does not exist"));
        if (tag.getName().equals(byId.getName())) {
            log.info("no update {}", tagDto);
            return tagDto;
        }
        Tag updated = tagDao.update(tag);
        TagDto updatedDto = tagMapper.toDto(updated);
        log.info("update {}", updatedDto);
        return updatedDto;
    }

    @Override
    public void delete(Long id) {
        Tag tag = tagDao.delete(id)
                .orElseThrow(() -> new NoSuchTagException("There is no Tag with ID " + id + " to delete"));
        log.info("delete {}", tag);
    }

}
