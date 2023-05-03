package ru.clevertec.ecl.giftcertificates.dao.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dao.TagDao;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The TagDaoImpl class implements the TagDao interface and provides the implementation for CRUD operations on the
 * {@link Tag} entity using Hibernate. It uses a {@link EntityManager} object to interact with the database.
 * For manage transactions it uses annotation {@link Transactional}
 */
@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    private final EntityManager entityManager;

    /**
     * Finds all {@link Tag} entities from database.
     *
     * @return a list of all Tag entities.
     */
    @Override
    public List<Tag> findAll() {
        return entityManager.createQuery("SELECT t FROM Tag t", Tag.class).getResultList();
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
        return Optional.ofNullable(entityManager.find(Tag.class, id));
    }

    /**
     * Saves one {@link Tag} entity in database.
     *
     * @param tag the Tag entity to save.
     * @return the saved Tag entity.
     */
    @Transactional
    @Override
    public Tag save(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    /**
     * Updates one {@link Tag} entity in database if it exists by ID, if not - saves new.
     *
     * @param tag the Tag entity to update.
     * @return the updated Tag entity.
     */
    @Transactional
    @Override
    public Tag update(Tag tag) {
        return entityManager.merge(tag);
    }

    /**
     * Deletes one {@link Tag} entity by ID in database.
     *
     * @param id the ID of the Tag entity to delete.
     * @return an {@link Optional} containing the deleted Tag entity, or an empty Optional if no such entity
     * exists in the database.
     */
    @Transactional
    @Override
    public Optional<Tag> delete(Long id) {
        Optional<Tag> tag = findById(id);
        if (tag.isEmpty()) {
            return Optional.empty();
        }
        Session session = entityManager.unwrap(Session.class);
        session.createNativeMutationQuery("DELETE FROM gift_certificate_tag WHERE tag_id = :id")
                .setParameter("id", id)
                .executeUpdate();
        session.createMutationQuery("DELETE FROM Tag WHERE id = :id")
                .setParameter("id", id)
                .executeUpdate();
        return tag;
    }

}
