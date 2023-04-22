package ru.clevertec.ecl.giftcertificates.dao.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.dao.TagDao;
import ru.clevertec.ecl.giftcertificates.exception.CannotDeleteTagException;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The TagDaoImpl class implements the TagDao interface and provides the implementation for CRUD operations on the
 * {@link Tag} entity using Hibernate. It uses a {@link SessionFactory} object to interact with the database
 * and manage transactions.
 */
@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    private final SessionFactory sessionFactory;

    /**
     * Finds all {@link Tag} entities from database.
     *
     * @return a list of all Tag entities.
     */
    @Override
    public List<Tag> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
        }
    }

    /**
     * Finds one {@link Tag} entity by ID from database.
     *
     * @param id the ID of the Tag entity.
     * @return an {@link Optional} containing the Tag entity with the specified ID, or an empty Optional if no such
     * entity exists in the database.
     */
    @Override
    public Optional<Tag> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Tag.class, id));
        }
    }

    /**
     * Saves one {@link Tag} entity in database.
     *
     * @param tag the Tag entity to save.
     * @return the saved Tag entity.
     */
    @Override
    public Tag save(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(tag);
            session.getTransaction().commit();
            return tag;
        }
    }

    /**
     * Updates one {@link Tag} entity in database if it exists by ID, if not - saves new.
     *
     * @param tag the Tag entity to update.
     * @return the updated Tag entity.
     */
    @Override
    public Tag update(Tag tag) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Tag merged = session.merge(tag);
            session.getTransaction().commit();
            return merged;
        }
    }

    /**
     * Deletes one {@link Tag} entity by ID in database.
     *
     * @param id the ID of the Tag entity to delete.
     * @return an {@link Optional} containing the deleted Tag entity, or an empty Optional if no such entity
     * exists in the database.
     * @throws CannotDeleteTagException if the Tag entity is associated with any {@link GiftCertificate} entities.
     */
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
