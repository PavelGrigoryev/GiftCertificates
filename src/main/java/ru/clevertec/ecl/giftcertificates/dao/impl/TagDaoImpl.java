package ru.clevertec.ecl.giftcertificates.dao.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.dao.TagDao;
import ru.clevertec.ecl.giftcertificates.exception.CannotDeleteTagException;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    private final SessionFactory sessionFactory;

    @Override
    public List<Tag> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
        }
    }

    @Override
    public Optional<Tag> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Tag.class, id));
        }
    }

    @Override
    public Tag save(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(tag);
            session.getTransaction().commit();
            return tag;
        }
    }

    @Override
    public Tag update(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Tag merged = session.merge(tag);
            session.getTransaction().commit();
            return merged;
        }
    }

    @Override
    public Optional<Tag> delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                Optional<Tag> tag = findById(id);
                if (tag.isEmpty()) {
                    return Optional.empty();
                }
                session.remove(tag.get());
                session.getTransaction().commit();
                return tag;
            } catch (ConstraintViolationException exception) {
                throw new CannotDeleteTagException("You cannot delete the Tag, first you need to delete" +
                                                   " the GiftCertificate that contains this Tag");
            }
        }
    }

}
