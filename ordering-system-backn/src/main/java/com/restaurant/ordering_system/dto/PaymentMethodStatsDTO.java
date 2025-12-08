package com.restaurant.ordering_system.dto;

import com.restaurant.ordering_system.enums.PaymentMethod;

import java.math.BigDecimal;

public class PaymentMethodStatsDTO {

    private PaymentMethod method;
    private BigDecimal totalAmount;

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
