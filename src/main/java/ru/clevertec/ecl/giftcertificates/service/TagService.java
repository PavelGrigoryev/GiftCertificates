package ru.clevertec.ecl.giftcertificates.service;

import ru.clevertec.ecl.giftcertificates.dto.TagDto;

import java.util.List;

public interface TagService {

    List<TagDto> findAll();

    TagDto findById(Long id);

    TagDto save(TagDto tagDto);

    TagDto update(TagDto tagDto);

    void delete(Long id);

}
