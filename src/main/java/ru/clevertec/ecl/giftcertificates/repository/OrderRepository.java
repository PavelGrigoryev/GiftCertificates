package ru.clevertec.ecl.giftcertificates.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.ecl.giftcertificates.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
