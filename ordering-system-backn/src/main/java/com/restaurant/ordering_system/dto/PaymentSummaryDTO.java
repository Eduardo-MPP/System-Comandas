package com.restaurant.ordering_system.dto;

import com.restaurant.ordering_system.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentSummaryDTO {
    private Long id;
    private BigDecimal amount;
    private PaymentMethod method;
    private String tableNumber;
    private LocalDateTime paidAt;
    private String waiterName; // 🔥 NUEVO

    // Constructors
    public PaymentSummaryDTO() {
    }

    public PaymentSummaryDTO(Long id, BigDecimal amount, PaymentMethod method, String tableNumber, 
                             LocalDateTime paidAt, String waiterName) { // 🔥 ACTUALIZADO
        this.id = id;
        this.amount = amount;
        this.method = method;
        this.tableNumber = tableNumber;
        this.paidAt = paidAt;
        this.waiterName = waiterName; // 🔥 NUEVO
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }

    // 🔥 NUEVO - Getter y Setter para waiterName
    public String getWaiterName() {
        return waiterName;
    }

    public void setWaiterName(String waiterName) {
        this.waiterName = waiterName;
    }
}
