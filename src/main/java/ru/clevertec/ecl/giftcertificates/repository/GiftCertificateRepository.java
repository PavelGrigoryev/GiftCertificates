package ru.clevertec.ecl.giftcertificates.repository;

import lombok.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.model.GiftCertificate;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The GiftCertificateRepository class implements the {@link JpaRepository} interface and provides the implementation
 * for CRUD operations on the {@link GiftCertificate}.
 */
@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    /**
     * Finds one {@link GiftCertificate} entity by ID with an {@link EntityGraph} that fetches its associated tags.
     *
     * @param id the ID of the GiftCertificate entity.
     * @return an {@link Optional} containing the GiftCertificate entity with the specified ID, or an empty Optional if
     * no such entity exists in the database.
     */
    @NonNull
    @Override
    @EntityGraph(attributePaths = {"tags"})
    Optional<GiftCertificate> findById(@NonNull Long id);

    /**
     * Finds all {@link GiftCertificate} by the specified parameters (All params are optional and can be
     * used in conjunction). It can find by tagName(if null finds all), by  part of name or description
     * (if null exclude this query), and can sort it by {@link Sort}.
     *
     * @param tagName the name of {@link Tag}.
     * @param part    the part of name or description of GiftCertificate.
     * @param sort    the sort by parameter of GiftCertificate.
     * @return a filtered and sorted by one or many parameters list of GiftCertificate entities from database.
     */
    @Query("""
            SELECT gc FROM GiftCertificate gc
            LEFT JOIN FETCH gc.tags t
            LEFT JOIN FETCH gc.tags WHERE (:tagName IS NULL OR t.name = :tagName)
            AND (:part IS NULL OR gc.name LIKE %:part% OR gc.description LIKE %:part%)
            """)
    List<GiftCertificate> findAllWithTags(String tagName, String part, Sort sort);

    /**
     * An overloaded method that does the same thing without a tagName.
     * See {@link #findAllWithTags(String, String, Sort)}
     *
     * @param part the part of name or description of GiftCertificate.
     * @param sort the sort by parameter of GiftCertificate.
     * @return a filtered and sorted by one or many parameters list of GiftCertificate entities from database.
     */
    @Query("""
            SELECT gc FROM GiftCertificate gc
            LEFT JOIN FETCH gc.tags t
            WHERE (:part IS NULL OR gc.name LIKE %:part% OR gc.description LIKE %:part%)
            """)
    List<GiftCertificate> findAllWithTags(String part, Sort sort);

}
