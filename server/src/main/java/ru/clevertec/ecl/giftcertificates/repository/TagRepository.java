package ru.clevertec.ecl.giftcertificates.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * The TagRepository class implements the {@link JpaRepository} interface and provides the implementation
 * for CRUD operations on the {@link Tag}.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    /**
     * Finds list of Tags by list of Tag names.
     *
     * @param names the list of {@link Tag} names.
     * @return the list of Tags.
     */
    List<Tag> findByNameIn(List<String> names);

    /**
     * Deletes relation with GiftCertificate in database.
     *
     * @param id the ID of the relation {@link Tag} with GiftCertificate to delete.
     */
    @Modifying
    @Query(value = "DELETE FROM gift_certificate_tag WHERE tag_id = :id", nativeQuery = true)
    void deleteRelation(Long id);

    /**
     * Deletes one {@link Tag} entity by ID in database. You can't delete a Tag without deleting its relationship.
     * Use first {@link #deleteRelation(Long)}
     *
     * @param id the ID of the Tag entity to delete.
     */
    @Override
    @Modifying
    @Query("DELETE FROM Tag WHERE id = :id")
    void deleteById(@NonNull Long id);

    /**
     * Get the most widely used {@link Tag} of a {@link ru.clevertec.ecl.giftcertificates.model.User} with the highest
     * cost of all orders.
     *
     * @param userId the ID of the User.
     * @return an {@link Optional} containing the Tag or an empty Optional if entity is not found in the database.
     */
    @Query(value = """
            SELECT t.* FROM tag t
            JOIN gift_certificate_tag gct ON t.id = gct.tag_id
            JOIN gift_certificate gc ON gct.gift_certificate_id = gc.id
            JOIN orders_gift_certificate ogc ON gc.id = ogc.gift_certificate_id
            JOIN orders o ON ogc.orders_id = o.id
            JOIN users u ON o.user_id = u.id
            WHERE u.id = :userId
            GROUP BY t.id
            ORDER BY SUM(o.price) DESC
            LIMIT 1
            """, nativeQuery = true)
    Optional<Tag> findTheMostWidelyUsedWithTheHighestCost(Long userId);

}
