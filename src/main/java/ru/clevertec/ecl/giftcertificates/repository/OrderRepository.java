package ru.clevertec.ecl.giftcertificates.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.model.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"giftCertificates"})
    List<Order> findAllByUserId(Long id, Pageable pageable);

    @EntityGraph(attributePaths = {"giftCertificates"})
    Optional<Order> findOrderByIdAndUserId(Long orderId, Long userId);

}
