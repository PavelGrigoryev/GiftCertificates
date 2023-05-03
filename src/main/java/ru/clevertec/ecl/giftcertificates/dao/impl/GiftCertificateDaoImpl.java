package ru.clevertec.ecl.giftcertificates.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The GiftCertificateDaoImpl class implements the GiftCertificateDao interface and provides the implementation for
 * CRUD operations on the {@link GiftCertificate} entity using Hibernate. It uses a {@link EntityManager} object to
 * interact with the database. For manage transactions it uses annotation {@link Transactional}
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final EntityManager entityManager;

    /**
     * Finds all {@link GiftCertificate} entities from database.
     *
     * @return a list of all GiftCertificate entities.
     */
    @Override
    public List<GiftCertificate> findAll() {
        return entityManager.createQuery("SELECT gc FROM GiftCertificate gc LEFT JOIN fetch gc.tags t",
                        GiftCertificate.class)
                .getResultList();
    }

    /**
     * Finds one {@link GiftCertificate} entity by ID from database.
     *
     * @param id the ID of the GiftCertificate entity.
     * @return an {@link Optional} containing the GiftCertificate entity with the specified ID, or an empty Optional if
     * no such entity exists in the database.
     */
    @Override
    public Optional<GiftCertificate> findById(Long id) {
        try {
            return Optional.of(
                    entityManager.createQuery("SELECT gc FROM GiftCertificate gc LEFT JOIN fetch gc.tags WHERE gc.id = :id",
                                    GiftCertificate.class)
                            .setParameter("id", id)
                            .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Finds all {@link GiftCertificate} by the specified parameters (All params are optional and can be
     * used in conjunction). It can find by tagName(if null finds all), by  part of name or description
     * (if null exclude this query), by date or name(if null exclude this query, if wrong input name default)
     * and can be ordered ascending or descending(if null or wrong input ascending by default).
     *
     * @param tagName the name of {@link Tag}.
     * @param part    the part of name or description of GiftCertificate.
     * @param sortBy  the sort by date or by name of GiftCertificate.
     * @param order   the order for sorting ascending(ASC) or descending(DESC).
     * @return a filtered and sorted by one or many parameters list of GiftCertificate entities from database.
     */
    @Override
    public List<GiftCertificate> findAllWithTags(String tagName, String part, String sortBy, String order) {
        StringBuilder sqlBuilder = new StringBuilder("SELECT gc FROM GiftCertificate gc LEFT JOIN fetch gc.tags t ");
        if (tagName != null) {
            sqlBuilder.append("JOIN fetch gc.tags WHERE t.name = :tagName ");
        }
        if (part != null) {
            sqlBuilder.append(tagName == null ? "WHERE " : "AND ");
            sqlBuilder.append("(gc.name LIKE :part OR gc.description LIKE :part) ");
        }
        if (sortBy != null || order != null) {
            sqlBuilder.append("date".equalsIgnoreCase(sortBy) ? "ORDER BY gc.createDate " : "ORDER BY gc.name ")
                    .append("DESC".equalsIgnoreCase(order) ? "DESC" : "ASC");
        }
        TypedQuery<GiftCertificate> query = entityManager.createQuery(sqlBuilder.toString(), GiftCertificate.class);
        if (tagName != null) {
            query.setParameter("tagName", tagName);
        }
        if (part != null) {
            query.setParameter("part", "%" + part + "%");
        }
        log.info("findAllWithTags HQL = " + sqlBuilder);
        return query.getResultList();
    }

    /**
     * Saves one {@link GiftCertificate} entity in database.
     *
     * @param giftCertificate the GiftCertificate entity to save.
     * @return the saved GiftCertificate entity.
     */
    @Transactional
    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        entityManager.persist(giftCertificate);
        return giftCertificate;
    }

    /**
     * Updates one {@link GiftCertificate} entity in database if it exists by ID, if not - saves new.
     *
     * @param giftCertificate the GiftCertificate entity to update in database.
     * @return the updated GiftCertificate entity.
     */
    @Transactional
    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        return entityManager.merge(giftCertificate);
    }

    /**
     * Deletes one {@link GiftCertificate} entity by ID in database.
     *
     * @param id the ID of the GiftCertificate entity to delete.
     * @return an {@link Optional} containing the deleted GiftCertificate entity, or an empty Optional if no such entity
     * exists in the database.
     */
    @Transactional
    @Override
    public Optional<GiftCertificate> delete(Long id) {
        Optional<GiftCertificate> giftCertificate = findById(id);
        if (giftCertificate.isEmpty()) {
            return Optional.empty();
        }
        entityManager.remove(giftCertificate.get());
        return giftCertificate;
    }

}
