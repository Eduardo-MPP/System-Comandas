package com.restaurant.ordering_system.model;

import com.restaurant.ordering_system.enums.PaymentMethod;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;
    
    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    private User cashier;
    
    @ManyToOne
    @JoinColumn(name = "cash_closing_id")
    private CashRegisterClosing cashClosing;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;
    
    @Column(nullable = false)
    private LocalDateTime paidAt = LocalDateTime.now();

    // Constructores
    public Payment() {
    }

    public Payment(Long id, Bill bill, User cashier, BigDecimal amount, PaymentMethod method, LocalDateTime paidAt) {
        this.id = id;
        this.bill = bill;
        this.cashier = cashier;
        this.amount = amount;
        this.method = method;
        this.paidAt = paidAt;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public User getCashier() {
        return cashier;
    }

    public void setCashier(User cashier) {
        this.cashier = cashier;
    }

    public CashRegisterClosing getCashClosing() {
        return cashClosing;
    }

    public void setCashClosing(CashRegisterClosing cashClosing) {
        this.cashClosing = cashClosing;
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

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
}
