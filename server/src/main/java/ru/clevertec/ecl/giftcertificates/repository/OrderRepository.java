package ru.clevertec.ecl.giftcertificates.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.model.Order;

import java.util.List;
import java.util.Optional;

/**
 * The OrderRepository class implements the {@link JpaRepository} interface and provides the implementation
 * for CRUD operations on the {@link Order}.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all {@link Order} by Users ID with an {@link EntityGraph} that fetches its associated gift certificates.
     * And sort it with pagination by {@link Pageable}.
     *
     * @param id       the ID of the {@link ru.clevertec.ecl.giftcertificates.model.User} entity.
     * @param pageable the Pageable.
     * @return the sorted list of Order entities with pagination.
     */
    @EntityGraph(attributePaths = {"giftCertificates"})
    List<Order> findAllByUserId(Long id, Pageable pageable);

    /**
     * Finds an {@link Order} linked to a {@link ru.clevertec.ecl.giftcertificates.model.User} entity.
     *
     * @param orderId the ID of the Order
     * @param userId the ID of the User
     * @return an {@link Optional} containing the Order entity with the specified ID, or an empty Optional if
     * no such entity exists in the database.
     */
    @EntityGraph(attributePaths = {"giftCertificates"})
    Optional<Order> findOrderByIdAndUserId(Long orderId, Long userId);

}
