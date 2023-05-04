package ru.clevertec.ecl.giftcertificates.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.model.Tag;

/**
 * The TagRepository class implements the {@link JpaRepository} interface and provides the implementation
 * for CRUD operations on the {@link Tag}.
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

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

}
