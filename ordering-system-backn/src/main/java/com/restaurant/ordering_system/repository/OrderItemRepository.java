package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.createdAt BETWEEN :start AND :end")
    List<OrderItem> findByOrderCreatedAtBetween(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
