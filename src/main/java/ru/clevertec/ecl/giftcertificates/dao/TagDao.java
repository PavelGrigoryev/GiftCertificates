package ru.clevertec.ecl.giftcertificates.dao;

import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDao {

    List<Tag> findAll();

    Optional<Tag> findById(Long id);

    Tag save(Tag tag);

    Tag update(Tag tag);

    Integer delete(Long id);

}
