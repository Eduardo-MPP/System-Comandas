package com.restaurant.ordering_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CashRegisterClosingResponse {
    private Long id;
    private String name;
    private String cashierName;
    private LocalDateTime closedAt;
    private BigDecimal totalAmount;
    private BigDecimal efectivoAmount;
    private BigDecimal tarjetaAmount;
    private BigDecimal transferenciaAmount;
    private Integer totalTransactions;

    // Constructors
    public CashRegisterClosingResponse() {
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

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName;
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
}
