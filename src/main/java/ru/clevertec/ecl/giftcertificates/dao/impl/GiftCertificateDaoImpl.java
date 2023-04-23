package ru.clevertec.ecl.giftcertificates.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The GiftCertificateDaoImpl class implements the GiftCertificateDao interface and provides the implementation for
 * CRUD operations on the {@link GiftCertificate} entity using Hibernate. It uses a {@link SessionFactory} object to
 * interact with the database and manage transactions.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final SessionFactory sessionFactory;

    /**
     * Finds all {@link GiftCertificate} entities from database.
     *
     * @return a list of all GiftCertificate entities.
     */
    @Override
    public List<GiftCertificate> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT gc FROM GiftCertificate gc", GiftCertificate.class)
                    .getResultList();
        }
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
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(GiftCertificate.class, id));
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
        try (Session session = sessionFactory.openSession()) {
            StringBuilder sqlBuilder = new StringBuilder("SELECT gc FROM GiftCertificate gc ");
            if (tagName != null) {
                sqlBuilder.append("JOIN gc.tags t WHERE t.name = :tagName ");
            }
            if (part != null) {
                sqlBuilder.append(tagName == null ? "WHERE " : "AND ");
                sqlBuilder.append("(gc.name LIKE :part OR gc.description LIKE :part) ");
            }
            if (sortBy != null || order != null) {
                sqlBuilder.append("date".equalsIgnoreCase(sortBy) ? "ORDER BY gc.createDate " : "ORDER BY gc.name ")
                        .append("DESC".equalsIgnoreCase(order) ? "DESC" : "ASC");
            }
            Query<GiftCertificate> query = session.createQuery(sqlBuilder.toString(), GiftCertificate.class);
            if (tagName != null) {
                query.setParameter("tagName", tagName);
            }
            if (part != null) {
                query.setParameter("part", "%" + part + "%");
            }
            log.info("findAllWithTags HQL = " + sqlBuilder);
            return query.getResultList();
        }
    }

    /**
     * Saves one {@link GiftCertificate} entity in database.
     *
     * @param giftCertificate the GiftCertificate entity to save.
     * @return the saved GiftCertificate entity.
     */
    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(giftCertificate);
            session.getTransaction().commit();
            return giftCertificate;
        }
    }

    /**
     * Updates one {@link GiftCertificate} entity in database if it exists by ID, if not - saves new.
     *
     * @param giftCertificate the GiftCertificate entity to update in database.
     * @return the updated GiftCertificate entity.
     */
    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            GiftCertificate merged = session.merge(giftCertificate);
            session.getTransaction().commit();
            return merged;
        }
    }

    /**
     * Deletes one {@link GiftCertificate} entity by ID in database.
     *
     * @param id the ID of the GiftCertificate entity to delete.
     * @return an {@link Optional} containing the deleted GiftCertificate entity, or an empty Optional if no such entity
     * exists in the database.
     */
    @Override
    public Optional<GiftCertificate> delete(Long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Optional<GiftCertificate> giftCertificate = findById(id);
            if (giftCertificate.isEmpty()) {
                return Optional.empty();
            }
            session.remove(giftCertificate.get());
            session.getTransaction().commit();
            return giftCertificate;
        }
    }

}
