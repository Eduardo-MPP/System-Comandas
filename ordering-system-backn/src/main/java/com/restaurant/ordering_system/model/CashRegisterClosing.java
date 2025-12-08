package com.restaurant.ordering_system.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "cash_register_closing")
public class CashRegisterClosing {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    private User cashier;
    
    @Column(name = "closed_at", nullable = false)
    private LocalDateTime closedAt;
    
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(name = "efectivo_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal efectivoAmount;
    
    @Column(name = "tarjeta_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal tarjetaAmount;
    
    @Column(name = "transferencia_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal transferenciaAmount;
    
    @Column(name = "total_transactions", nullable = false)
    private Integer totalTransactions;
    
    @OneToMany(mappedBy = "cashClosing")
    private List<Payment> payments;

    // Constructors
    public CashRegisterClosing() {
    }

    public CashRegisterClosing(String name, User cashier, LocalDateTime closedAt, BigDecimal totalAmount, 
                               BigDecimal efectivoAmount, BigDecimal tarjetaAmount, BigDecimal transferenciaAmount, 
                               Integer totalTransactions) {
        this.name = name;
        this.cashier = cashier;
        this.closedAt = closedAt;
        this.totalAmount = totalAmount;
        this.efectivoAmount = efectivoAmount;
        this.tarjetaAmount = tarjetaAmount;
        this.transferenciaAmount = transferenciaAmount;
        this.totalTransactions = totalTransactions;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getCashier() {
        return cashier;
    }

    public void setCashier(User cashier) {
        this.cashier = cashier;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getEfectivoAmount() {
        return efectivoAmount;
    }

    public void setEfectivoAmount(BigDecimal efectivoAmount) {
        this.efectivoAmount = efectivoAmount;
    }

    public BigDecimal getTarjetaAmount() {
        return tarjetaAmount;
    }

    public void setTarjetaAmount(BigDecimal tarjetaAmount) {
        this.tarjetaAmount = tarjetaAmount;
    }

    public BigDecimal getTransferenciaAmount() {
        return transferenciaAmount;
    }

    public void setTransferenciaAmount(BigDecimal transferenciaAmount) {
        this.transferenciaAmount = transferenciaAmount;
    }

    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}
