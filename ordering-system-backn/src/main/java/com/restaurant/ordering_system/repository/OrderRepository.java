package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.enums.OrderStatus;
import com.restaurant.ordering_system.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusIn(List<OrderStatus> statuses);

    List<Order> findByWaiterIdAndStatus(Long waiterId, OrderStatus status);

    Optional<Order> findByTableIdAndStatusNot(Long tableId, OrderStatus status);

    List<Order> findByStatus(OrderStatus status);

    Optional<Order> findByTableIdAndStatus(Long tableId, OrderStatus status);

    // cuando quieras usarlo:
    // long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
