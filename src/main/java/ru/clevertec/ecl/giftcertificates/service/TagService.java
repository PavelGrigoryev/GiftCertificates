package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.TagDto;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;

public interface TagService {

    TagDto findById(Long id);

    List<TagDto> findAll();

    TagDto save(TagDto tagDto);

    TagDto update(TagDto tagDto);

    void delete(Long id);

    List<Tag> findByNameIn(List<String> names);

}
