package com.restaurant.ordering_system.repository;

import com.restaurant.ordering_system.model.Payment;
import com.restaurant.ordering_system.model.CashRegisterClosing; // 🔥 AGREGADO
import com.restaurant.ordering_system.enums.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByMethod(PaymentMethod method);
    
    @Query("SELECT p FROM Payment p WHERE p.paidAt BETWEEN :start AND :end")
    List<Payment> findByPaidAtBetween(LocalDateTime start, LocalDateTime end);
    
    // Métodos para cierre de caja
    List<Payment> findByCashClosingIsNullOrderByPaidAtDesc();
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.cashClosing IS NULL AND p.method = :method")
    BigDecimal sumAmountByMethodAndCashClosingIsNull(@Param("method") PaymentMethod method);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.cashClosing IS NULL")
    Long countByCashClosingIsNull();
    
    // 🔥 NUEVO - Para obtener transacciones de un cierre específico
    List<Payment> findByCashClosingOrderByPaidAtDesc(CashRegisterClosing cashClosing);
}
