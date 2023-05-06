package ru.clevertec.ecl.giftcertificates.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"orders.giftCertificates"})
    Optional<User> findById(@NonNull Long id);

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"orders.giftCertificates"})
    List<User> findAll();

}
