package ru.clevertec.ecl.giftcertificates.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final SessionFactory sessionFactory;

    @Override
    public List<GiftCertificate> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT gc FROM GiftCertificate gc", GiftCertificate.class)
                    .getResultList();
        }
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(GiftCertificate.class, id));
        }
    }

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

    @Override
    public GiftCertificate save(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(giftCertificate);
            session.getTransaction().commit();
            return giftCertificate;
        }
    }

    @Override
    public GiftCertificate update(GiftCertificate giftCertificate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            GiftCertificate merged = session.merge(giftCertificate);
            session.getTransaction().commit();
            return merged;
        }
    }

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
