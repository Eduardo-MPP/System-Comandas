package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.CashRegisterClosing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CashRegisterClosingRepository extends JpaRepository<CashRegisterClosing, Long> {
    List<CashRegisterClosing> findAllByOrderByClosedAtDesc();
    List<CashRegisterClosing> findByCashierIdOrderByClosedAtDesc(Long cashierId);
    List<CashRegisterClosing> findByClosedAtBetweenOrderByClosedAtDesc(LocalDateTime start, LocalDateTime end);
}
