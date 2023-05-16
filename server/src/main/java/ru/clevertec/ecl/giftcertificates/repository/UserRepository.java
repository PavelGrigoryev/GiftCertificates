package ru.clevertec.ecl.giftcertificates.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.model.User;

import java.util.List;
import java.util.Optional;

/**
 * The UserRepository class implements the {@link JpaRepository} interface and provides the implementation
 * for CRUD operations on the {@link User}.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds one {@link User} entity by ID with an {@link EntityGraph} that fetches its associated orders and
     * gift certificates.
     *
     * @param id the ID of the User entity.
     * @return an {@link Optional} containing the User entity with the specified ID, or an empty Optional if
     * no such entity exists in the database.
     */
    @NonNull
    @Override
    @EntityGraph(attributePaths = {"orders.giftCertificates"})
    Optional<User> findById(@NonNull Long id);

    /**
     * Finds all {@link User} with an {@link EntityGraph} that fetches its associated orders and gift certificates.
     *
     * @return the list of User entities from database.
     */
    @NonNull
    @Override
    @EntityGraph(attributePaths = {"orders.giftCertificates"})
    List<User> findAll();

}
