package ru.clevertec.ecl.giftcertificates.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
            Root<GiftCertificate> root = criteriaQuery.from(GiftCertificate.class);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        }
    }

    @Override
    public Optional<GiftCertificate> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(GiftCertificate.class, id));
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
