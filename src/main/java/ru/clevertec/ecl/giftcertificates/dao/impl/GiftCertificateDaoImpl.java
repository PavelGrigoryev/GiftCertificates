package ru.clevertec.ecl.giftcertificates.dao.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.dao.GiftCertificateDao;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;

import java.util.List;
import java.util.Optional;

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
    public List<GiftCertificate> findAllByTagName(String tagName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("SELECT gc FROM GiftCertificate gc JOIN gc.tags t WHERE t.name = :tagName",
                            GiftCertificate.class)
                    .setParameter("tagName", tagName)
                    .getResultList();
        }
    }

    @Override
    public List<GiftCertificate> findAllByPartOfNameOrDescription(String part) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM GiftCertificate WHERE name LIKE :part OR description LIKE :part",
                            GiftCertificate.class)
                    .setParameter("part", "%" + part + "%")
                    .getResultList();
        }
    }

    @Override
    public List<GiftCertificate> findAllSortedByCreateDateAndName(boolean asc) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM GiftCertificate ORDER BY createDate " + (asc ? "ASC" : "DESC")
                                       + ", name " + (asc ? "ASC" : "DESC"), GiftCertificate.class).getResultList();
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
