package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Optional<Bill> findByTableId(Long tableId);
    
    Optional<Bill> findByOrderId(Long orderId);
    
    // 🔥 CORREGIDO: Buscar bills donde NO existe un Payment asociado
    @Query("SELECT b FROM Bill b WHERE NOT EXISTS (SELECT p FROM Payment p WHERE p.bill.id = b.id) ORDER BY b.createdAt ASC")
    List<Bill> findPendingBills();
    
    @Query("SELECT b FROM Bill b WHERE b.createdAt BETWEEN :start AND :end")
    List<Bill> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
