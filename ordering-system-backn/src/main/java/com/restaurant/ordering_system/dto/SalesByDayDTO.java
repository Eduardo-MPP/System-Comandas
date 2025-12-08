package com.restaurant.ordering_system.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SalesByDayDTO {

    private LocalDate date;
    private BigDecimal totalAmount;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
