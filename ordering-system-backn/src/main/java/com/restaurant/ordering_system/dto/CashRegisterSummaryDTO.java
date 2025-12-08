package com.restaurant.ordering_system.dto;

import java.math.BigDecimal;
import java.util.List;

public class CashRegisterSummaryDTO {
    private BigDecimal totalAmount;
    private BigDecimal efectivoAmount;
    private BigDecimal tarjetaAmount;
    private BigDecimal transferenciaAmount;
    private Integer totalTransactions;
    private List<PaymentSummaryDTO> payments;

    // Constructors
    public CashRegisterSummaryDTO() {
    }

    // Getters and Setters
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

    public List<PaymentSummaryDTO> getPayments() {
        return payments;
    }

    public void setPayments(List<PaymentSummaryDTO> payments) {
        this.payments = payments;
    }
}
