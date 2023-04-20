package ru.clevertec.ecl.giftcertificates.dao.impl;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.dao.TagDao;
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
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Tag> criteriaQuery = criteriaBuilder.createQuery(Tag.class);
            Root<Tag> root = criteriaQuery.from(Tag.class);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
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
            session.beginTransaction();
            Optional<Tag> tag = findById(id);
            if (tag.isEmpty()) {
                return Optional.empty();
            }
            session.remove(tag.get());
            session.getTransaction().commit();
            return tag;
        }
    }

}
