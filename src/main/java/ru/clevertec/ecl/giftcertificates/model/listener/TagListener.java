package ru.clevertec.ecl.giftcertificates.model.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.clevertec.ecl.giftcertificates.model.Tag;

public class TagListener {

    @PrePersist
    public void prePersist(Tag tag) {
        tag.setOperation("INSERT");
    }

    @PreUpdate
    public void preUpdate(Tag tag) {
        tag.setOperation("UPDATE");
    }

}
